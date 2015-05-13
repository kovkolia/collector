/**
 * This file is part of Graylog.
 *
 * Graylog is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Graylog is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Graylog.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.graylog.collector.inputs.eventlog;

import com.google.inject.assistedinject.Assisted;
import org.graylog.collector.MessageBuilder;
import org.graylog.collector.buffer.Buffer;
import org.graylog.collector.config.ConfigurationUtils;
import org.graylog.collector.file.ChunkReader;
import org.graylog.collector.inputs.InputService;
import org.hyperic.sigar.win32.EventLogThread;

import javax.inject.Inject;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

public class WindowsEventlogInput extends InputService {
    private final WindowsEventlogInputConfiguration configuration;
    private final Buffer buffer;
    private final CountDownLatch stopLatch = new CountDownLatch(1);

    public interface Factory extends InputService.Factory<WindowsEventlogInput, WindowsEventlogInputConfiguration> {
        WindowsEventlogInput create(WindowsEventlogInputConfiguration configuration);
    }

    @Inject
    public WindowsEventlogInput(@Assisted WindowsEventlogInputConfiguration configuration, Buffer buffer) {
        this.configuration = configuration;
        this.buffer = buffer;
    }

    @Override
    protected void triggerShutdown() {
        stopLatch.countDown();
    }

    @Override
    protected void run() throws Exception {
        final MessageBuilder messageBuilder = new MessageBuilder().input(getId()).outputs(getOutputs());
        final EventLogThread logThread = EventLogThread.getInstance(configuration.getSourceName());

        logThread.add(new WindowsEventlogHandler(messageBuilder, buffer));
        logThread.setInterval(configuration.getPollInterval());
        logThread.doStart();

        stopLatch.await();

        logThread.doStop();
    }

    @Override
    public String getId() {
        return configuration.getId();
    }

    @Override
    public Set<String> getOutputs() {
        return configuration.getOutputs();
    }

    @Override
    public void setReaderFinished(ChunkReader chunkReader) {

    }

    @Override
    public String toString() {
        return ConfigurationUtils.toString(configuration, this);
    }
}