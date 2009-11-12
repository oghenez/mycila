/**
 * Copyright (C) 2009 Mathieu Carbou <mathieu.carbou@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mycila.event.api;

import java.io.Serializable;

import static com.mycila.event.api.util.Ensure.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Topics {

    private Topics() {
    }

    public static Topic topic(String name) {
        return new TopicImpl(name);
    }

    private static final class TopicImpl extends AbstractTopicMatcher implements Topic, Serializable {

        private static final long serialVersionUID = 0;

        private final String name;

        private TopicImpl(String name) {
            this.name = notNull(name, "Topic name");
        }

        public String name() {
            return name;
        }

        @Override
        public boolean matches(Topic topic) {
            return equals(topic);
        }

        @Override
        public boolean equals(Object other) {
            return other instanceof TopicImpl && ((TopicImpl) other).name.equals(name);
        }

        @Override
        public int hashCode() {
            return 31 * name.hashCode();
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public static AbstractTopicMatcher topics(String pattern) {
        return matcher(AntTopicMatcher.forPattern(pattern));
    }

    /**
     * Returns a matcher which matches any input.
     */
    public static AbstractTopicMatcher any() {
        return ANY;
    }

    private static final AbstractTopicMatcher ANY = topics("**");

    public static AbstractTopicMatcher matcher(final TopicMatcher matcher) {
        return new Delegate(matcher);
    }

    private static final class Delegate extends AbstractTopicMatcher implements Serializable {
        private static final long serialVersionUID = 0;
        final TopicMatcher matcher;

        private Delegate(TopicMatcher matcher) {
            this.matcher = notNull(matcher, "TopicMatcher");
        }

        @Override
        public boolean matches(Topic t) {
            return matcher.matches(notNull(t, "Topic"));
        }

        @Override
        public boolean equals(Object other) {
            return other instanceof Delegate && ((Delegate) other).matcher.equals(matcher);
        }

        @Override
        public int hashCode() {
            return matcher.hashCode();
        }

        @Override
        public String toString() {
            return matcher.toString();
        }
    }

    /**
     * Inverts the given matcher.
     */
    public static AbstractTopicMatcher not(TopicMatcher matcher) {
        return new Not(matcher);
    }

    private static final class Not extends AbstractTopicMatcher implements Serializable {
        private static final long serialVersionUID = 0;
        final TopicMatcher matcher;

        private Not(TopicMatcher matcher) {
            this.matcher = notNull(matcher, "TopicMatcher");
        }

        @Override
        public boolean matches(Topic t) {
            return !matcher.matches(notNull(t, "Topic"));
        }

        @Override
        public boolean equals(Object other) {
            return other instanceof Not && ((Not) other).matcher.equals(matcher);
        }

        @Override
        public int hashCode() {
            return -matcher.hashCode();
        }

        @Override
        public String toString() {
            return "not(" + matcher + ")";
        }
    }

    public static AbstractTopicMatcher only(String exactTopicName) {
        return only(topic(exactTopicName));
    }

    public static AbstractTopicMatcher only(Topic topic) {
        return new Only(notNull(topic, "Topic"));
    }

    private static class Only extends AbstractTopicMatcher implements Serializable {
        private static final long serialVersionUID = 0;
        private final Topic topic;

        public Only(Topic topic) {
            this.topic = topic;
        }

        @Override
        public boolean matches(Topic other) {
            return topic.equals(other);
        }

        @Override
        public boolean equals(Object other) {
            return other instanceof Only && ((Only) other).topic.equals(topic);
        }

        @Override
        public int hashCode() {
            return 37 * topic.hashCode();
        }

        @Override
        public String toString() {
            return topic.name();
        }
    }

}
