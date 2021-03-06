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
package org.graylog.collector.buffer;

import com.typesafe.config.Config;

import javax.inject.Inject;

public class MessageBufferConfiguration {
    private static int SIZE = 128;

    private final int size;

    @Inject
    public MessageBufferConfiguration(Config config) {
        if (config.hasPath("message-buffer-size")) {
            this.size = config.getInt("message-buffer-size");
        } else {
            this.size = SIZE;
        }
    }

    public int getSize() {
        return size;
    }
}
