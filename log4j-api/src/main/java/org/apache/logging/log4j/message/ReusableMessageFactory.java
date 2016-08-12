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
package org.apache.logging.log4j.message;

import java.io.Serializable;

import org.apache.logging.log4j.util.PerformanceSensitive;

/**
 * Implementation of the {@link MessageFactory} interface that avoids allocating temporary objects where possible.
 * Message instances are cached in a ThreadLocal and reused when a new message is requested within the same thread.
 * @see ParameterizedMessageFactory
 * @see ReusableSimpleMessage
 * @see ReusableObjectMessage
 * @see ReusableParameterizedMessage
 * @since 2.6
 */
@PerformanceSensitive("allocation")
public final class ReusableMessageFactory implements MessageFactory2, Serializable {

    /**
     * Instance of ReusableMessageFactory..
     */
    public static final ReusableMessageFactory INSTANCE = new ReusableMessageFactory();

    private static final long serialVersionUID = -8970940216592525651L;
    private static ThreadLocal<ReusableParameterizedMessage> threadLocalParameterized = new ThreadLocal<ReusableParameterizedMessage>();
    private static ThreadLocal<ReusableSimpleMessage> threadLocalSimpleMessage = new ThreadLocal<ReusableSimpleMessage>();
    private static ThreadLocal<ReusableObjectMessage> threadLocalObjectMessage = new ThreadLocal<ReusableObjectMessage>();

    /**
     * Constructs a message factory.
     */
    public ReusableMessageFactory() {
        super();
    }

    private static ReusableParameterizedMessage getParameterized() {
        ReusableParameterizedMessage result = threadLocalParameterized.get();
        if (result == null) {
            result = new ReusableParameterizedMessage();
            threadLocalParameterized.set(result);
        }
        return result;
    }

    private static ReusableSimpleMessage getSimple() {
        ReusableSimpleMessage result = threadLocalSimpleMessage.get();
        if (result == null) {
            result = new ReusableSimpleMessage();
            threadLocalSimpleMessage.set(result);
        }
        return result;
    }

    private static ReusableObjectMessage getObject() {
        ReusableObjectMessage result = threadLocalObjectMessage.get();
        if (result == null) {
            result = new ReusableObjectMessage();
            threadLocalObjectMessage.set(result);
        }
        return result;
    }

    @Override
    public Message newMessage(final CharSequence charSequence) {
        final ReusableSimpleMessage result = getSimple();
        result.set(charSequence);
        return result;
    }

    /**
     * Creates {@link ReusableParameterizedMessage} instances.
     *
     * @param message The message pattern.
     * @param params The message parameters.
     * @return The Message.
     *
     * @see MessageFactory#newMessage(String, Object...)
     */
    @Override
    public Message newMessage(final String message, final Object... params) {
        return getParameterized().set(message, params);
    }

    @Override
    public Message newMessage(final String message, final Object p0) {
        return getParameterized().set(message, p0);
    }

    @Override
    public Message newMessage(final String message, final Object p0, final Object p1) {
        return getParameterized().set(message, p0, p1);
    }

    @Override
    public Message newMessage(final String message, final Object p0, final Object p1, final Object p2) {
        return getParameterized().set(message, p0, p1, p2);
    }

    @Override
    public Message newMessage(final String message, final Object p0, final Object p1, final Object p2,
            final Object p3) {
        return getParameterized().set(message, p0, p1, p2, p3);
    }

    @Override
    public Message newMessage(final String message, final Object p0, final Object p1, final Object p2, final Object p3,
            final Object p4) {
        return getParameterized().set(message, p0, p1, p2, p3, p4);
    }

    @Override
    public Message newMessage(final String message, final Object p0, final Object p1, final Object p2, final Object p3,
            final Object p4, final Object p5) {
        return getParameterized().set(message, p0, p1, p2, p3, p4, p5);
    }

    @Override
    public Message newMessage(final String message, final Object p0, final Object p1, final Object p2, final Object p3,
            final Object p4, final Object p5, final Object p6) {
        return getParameterized().set(message, p0, p1, p2, p3, p4, p5, p6);
    }

    @Override
    public Message newMessage(final String message, final Object p0, final Object p1, final Object p2, final Object p3,
            final Object p4, final Object p5, final Object p6, final Object p7) {
        return getParameterized().set(message, p0, p1, p2, p3, p4, p5, p6, p7);
    }

    @Override
    public Message newMessage(final String message, final Object p0, final Object p1, final Object p2, final Object p3,
            final Object p4, final Object p5, final Object p6, final Object p7, final Object p8) {
        return getParameterized().set(message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
    }

    @Override
    public Message newMessage(final String message, final Object p0, final Object p1, final Object p2, final Object p3,
            final Object p4, final Object p5, final Object p6, final Object p7, final Object p8, final Object p9) {
        return getParameterized().set(message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
    }

    /**
     * Creates {@link ReusableSimpleMessage} instances.
     *
     * @param message The message String.
     * @return The Message.
     *
     * @see MessageFactory#newMessage(String)
     */
    @Override
    public Message newMessage(final String message) {
        final ReusableSimpleMessage result = getSimple();
        result.set(message);
        return result;
    }


    /**
     * Creates {@link ReusableObjectMessage} instances.
     *
     * @param message The message Object.
     * @return The Message.
     *
     * @see MessageFactory#newMessage(Object)
     */
    @Override
    public Message newMessage(final Object message) {
        final ReusableObjectMessage result = getObject();
        result.set(message);
        return result;
    }
}
