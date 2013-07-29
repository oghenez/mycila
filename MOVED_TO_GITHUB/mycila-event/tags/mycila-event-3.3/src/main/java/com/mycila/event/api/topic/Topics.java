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

package com.mycila.event.api.topic;

import java.io.Serializable;
import java.util.Arrays;
import java.util.UUID;

import static com.mycila.event.api.Ensure.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Topics {

    private Topics() {
    }

    public static Topic[] topics(String... names) {
        notNull(names, "Topic names");
        Topic[] topics = new Topic[names.length];
        for (int i = 0, length = topics.length; i < length; i++)
            topics[i] = topic(names[i]);
        return topics;
    }

    public static Topic topic(String name) {
        return new TopicImpl(name);
    }

    public static Topic temporary() {
        return new TopicImpl("temp/" + UUID.randomUUID().toString());
    }

    private static final class TopicImpl extends TopicMatcherSkeleton implements Topic, Serializable {

        private static final long serialVersionUID = 0;

        private final String name;

        private TopicImpl(String name) {
            this.name = notNull(name, "Topic name");
        }

        public String getName() {
            return name;
        }

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

    public static TopicMatcherSkeleton matching(String pattern) {
        return matcher(AntTopicMatcher.forPattern(pattern));
    }

    /**
     * Returns a matcher which matches any input.
     */
    public static TopicMatcherSkeleton any() {
        return ANY;
    }

    private static final TopicMatcherSkeleton ANY = matching("**");

    public static TopicMatcherSkeleton matcher(final TopicMatcher matcher) {
        return new Delegate(matcher);
    }

    private static final class Delegate extends TopicMatcherSkeleton implements Serializable {
        private static final long serialVersionUID = 0;
        final TopicMatcher matcher;

        private Delegate(TopicMatcher matcher) {
            this.matcher = notNull(matcher, "TopicMatcher");
        }

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
    public static TopicMatcherSkeleton not(TopicMatcher matcher) {
        return new Not(matcher);
    }

    private static final class Not extends TopicMatcherSkeleton implements Serializable {
        private static final long serialVersionUID = 0;
        final TopicMatcher matcher;

        private Not(TopicMatcher matcher) {
            this.matcher = notNull(matcher, "TopicMatcher");
        }

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

    public static TopicMatcherSkeleton only(String exactTopicName) {
        return only(topic(exactTopicName));
    }

    public static TopicMatcherSkeleton only(Topic topic) {
        return new Only(notNull(topic, "Topic"));
    }

    private static class Only extends TopicMatcherSkeleton implements Serializable {
        private static final long serialVersionUID = 0;
        private final Topic topic;

        public Only(Topic topic) {
            this.topic = topic;
        }

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
            return topic.getName();
        }
    }

    public static TopicMatcherSkeleton anyOf(String... patterns) {
        notNull(patterns, "Topic patterns");
        TopicMatcher[] matchers = new TopicMatcher[patterns.length];
        for (int i = 0, length = matchers.length; i < length; i++)
            matchers[i] = matching(patterns[i]);
        return anyOf(matchers);
    }

    public static TopicMatcherSkeleton anyOf(TopicMatcher... matchers) {
        return new AnyOf(matchers);
    }

    private static final class AnyOf extends TopicMatcherSkeleton implements Serializable {
        private static final long serialVersionUID = 0;
        final TopicMatcher[] matchers;

        private AnyOf(TopicMatcher... matchers) {
            this.matchers = notNull(matchers, "TopicMatcher list");
        }

        public boolean matches(Topic t) {
            notNull(t, "Topic");
            for (TopicMatcher matcher : matchers)
                if (matcher.matches(t))
                    return true;
            return false;
        }

        @Override
        public boolean equals(Object other) {
            return other instanceof AnyOf && Arrays.equals(((AnyOf) other).matchers, matchers);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(matchers);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder("anyOf(");
            int len = matchers.length;
            if (len > 0) {
                sb.append(matchers[0]);
                for (int i = 1; i < len; i++)
                    sb.append(", ").append(matchers[i]);
            }
            return sb.append(")").toString();
        }
    }

}
