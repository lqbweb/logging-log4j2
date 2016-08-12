/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache license, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the license for the specific language governing permissions and
 * limitations under the license.
 */
package org.apache.logging.log4j;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link CloseableThreadContext}.
 *
 * @since 2.6
 */
public class CloseableThreadContextTest {

    private final String key = "key";
    private final String value = "value";

    @Before
    public void setUp() throws Exception {
        ThreadContext.clearAll();
    }

    @Test
    public void shouldAddAnEntryToTheMap() throws Exception {
        final CloseableThreadContext.Instance ignored = CloseableThreadContext.put(key, value);
        try {
            assertThat(ThreadContext.get(key), is(value));
        } finally {
            ignored.close();
        }
    }

    @Test
    public void shouldAddTwoEntriesToTheMap() throws Exception {
        final String key2 = "key2";
        final String value2 = "value2";
        final CloseableThreadContext.Instance ignored = CloseableThreadContext.put(key, value).put(key2, value2);
        try {
            assertThat(ThreadContext.get(key), is(value));
            assertThat(ThreadContext.get(key2), is(value2));
        } finally {
            ignored.close();
        }
    }

    @Test
    public void shouldNestEntries() throws Exception {
        final String oldValue = "oldValue";
        final String innerValue = "innerValue";
        ThreadContext.put(key, oldValue);
        final CloseableThreadContext.Instance ignored = CloseableThreadContext.put(key, value);
        try {
            assertThat(ThreadContext.get(key), is(value));
            final CloseableThreadContext.Instance ignored2 = CloseableThreadContext.put(key, innerValue);
            try {
                assertThat(ThreadContext.get(key), is(innerValue));
            } finally {
                ignored2.close();
            }
            assertThat(ThreadContext.get(key), is(value));
        } finally {
            ignored.close();
        }
        assertThat(ThreadContext.get(key), is(oldValue));
    }

    @Test
    public void shouldPreserveOldEntriesFromTheMapWhenAutoClosed() throws Exception {
        final String oldValue = "oldValue";
        ThreadContext.put(key, oldValue);
        final CloseableThreadContext.Instance ignored = CloseableThreadContext.put(key, value);
        try {
            assertThat(ThreadContext.get(key), is(value));
        } finally {
            ignored.close();
        }
        assertThat(ThreadContext.get(key), is(oldValue));
    }

    @Test
    public void ifTheSameKeyIsAddedTwiceTheOriginalShouldBeUsed() throws Exception {
        final String oldValue = "oldValue";
        final String secondValue = "innerValue";
        ThreadContext.put(key, oldValue);
        final CloseableThreadContext.Instance ignored = CloseableThreadContext.put(key, value).put(key, secondValue);
        try {
            assertThat(ThreadContext.get(key), is(secondValue));
        } finally {
            ignored.close();
        }
        assertThat(ThreadContext.get(key), is(oldValue));
    }

    @Test
    public void shouldPushAndPopAnEntryToTheStack() throws Exception {
        final String message = "message";
        final CloseableThreadContext.Instance ignored = CloseableThreadContext.push(message);
        try {
            assertThat(ThreadContext.peek(), is(message));
        } finally {
            ignored.close();
        }
        assertThat(ThreadContext.peek(), is(""));
    }

    @Test
    public void shouldPushAndPopTwoEntriesToTheStack() throws Exception {
        final String message1 = "message1";
        final String message2 = "message2";
        final CloseableThreadContext.Instance ignored = CloseableThreadContext.push(message1).push(message2);
        try {
            assertThat(ThreadContext.peek(), is(message2));
        } finally {
            ignored.close();
        }
        assertThat(ThreadContext.peek(), is(""));
    }

    @Test
    public void shouldPushAndPopAParameterizedEntryToTheStack() throws Exception {
        final String parameterizedMessage = "message {}";
        final String parameterizedMessageParameter = "param";
        final String formattedMessage = parameterizedMessage.replace("{}", parameterizedMessageParameter);
        final CloseableThreadContext.Instance ignored = CloseableThreadContext.push(parameterizedMessage,
                parameterizedMessageParameter);
        try {
            assertThat(ThreadContext.peek(), is(formattedMessage));
        } finally {
            ignored.close();
        }
        assertThat(ThreadContext.peek(), is(""));
    }

    @Test
    public void shouldRemoveAnEntryFromTheMapWhenAutoClosed() throws Exception {
        final CloseableThreadContext.Instance ignored = CloseableThreadContext.put(key, value);
        try {
            assertThat(ThreadContext.get(key), is(value));
        } finally {
            ignored.close();
        }
        assertThat(ThreadContext.containsKey(key), is(false));
    }

    @Test
    public void shouldAddEntriesToBothStackAndMap() throws Exception {
        final String stackValue = "something";
        final CloseableThreadContext.Instance ignored = CloseableThreadContext.put(key, value).push(stackValue);
        try {
            assertThat(ThreadContext.get(key), is(value));
            assertThat(ThreadContext.peek(), is(stackValue));
        } finally {
            ignored.close();
        }
        assertThat(ThreadContext.containsKey(key), is(false));
        assertThat(ThreadContext.peek(), is(""));
    }

    @Test
    public void canReuseCloseableThreadContext() throws Exception {
        final String stackValue = "something";
        // Create a ctc and close it
        final CloseableThreadContext.Instance ctc = CloseableThreadContext.push(stackValue).put(key, value);
        assertThat(ThreadContext.get(key), is(value));
        assertThat(ThreadContext.peek(), is(stackValue));
        ctc.close();

        assertThat(ThreadContext.containsKey(key), is(false));
        assertThat(ThreadContext.peek(), is(""));

        final String anotherKey = "key2";
        final String anotherValue = "value2";
        final String anotherStackValue = "something else";
        // Use it again
        ctc.push(anotherStackValue).put(anotherKey, anotherValue);
        assertThat(ThreadContext.get(anotherKey), is(anotherValue));
        assertThat(ThreadContext.peek(), is(anotherStackValue));
        ctc.close();

        assertThat(ThreadContext.containsKey(anotherKey), is(false));
        assertThat(ThreadContext.peek(), is(""));
    }

    @Test
    public void closeIsIdempotent() throws Exception {

        final String originalMapValue = "map to keep";
        final String originalStackValue = "stack to keep";
        ThreadContext.put(key, originalMapValue);
        ThreadContext.push(originalStackValue);

        final String newMapValue = "temp map value";
        final String newStackValue = "temp stack to keep";
        final CloseableThreadContext.Instance ctc = CloseableThreadContext.push(newStackValue).put(key, newMapValue);

        ctc.close();
        assertThat(ThreadContext.get(key), is(originalMapValue));
        assertThat(ThreadContext.peek(), is(originalStackValue));

        ctc.close();
        assertThat(ThreadContext.get(key), is(originalMapValue));
        assertThat(ThreadContext.peek(), is(originalStackValue));
    }
}
