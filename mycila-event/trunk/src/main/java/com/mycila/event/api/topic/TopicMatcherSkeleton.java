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

import static com.mycila.event.api.Ensure.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public abstract class TopicMatcherSkeleton implements TopicMatcher {

    public final TopicMatcherSkeleton or(TopicMatcher other) {
        return new OrMatcher(this, notNull(other, "TopicMatcher"));
    }

    public final TopicMatcherSkeleton and(TopicMatcher other) {
        return new AndMatcher(this, notNull(other, "TopicMatcher"));
    }

    @Override
    public abstract boolean equals(Object other);

    @Override
    public abstract int hashCode();

    @Override
    public abstract String toString();

    private static class AndMatcher extends TopicMatcherSkeleton implements Serializable {
        private static final long serialVersionUID = 0;
        private final TopicMatcher a, b;

        public AndMatcher(TopicMatcher a, TopicMatcher b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public boolean matches(Topic t) {
            notNull(t, "Topic");
            return a.matches(t) && b.matches(t);
        }

        @Override
        public boolean equals(Object other) {
            return other instanceof AndMatcher
                    && ((AndMatcher) other).a.equals(a)
                    && ((AndMatcher) other).b.equals(b);
        }

        @Override
        public int hashCode() {
            return 41 * (a.hashCode() ^ b.hashCode());
        }

        @Override
        public String toString() {
            return "and(" + a + ", " + b + ")";
        }
    }

    private static class OrMatcher extends TopicMatcherSkeleton implements Serializable {
        private static final long serialVersionUID = 0;
        private final TopicMatcher a, b;

        public OrMatcher(TopicMatcher a, TopicMatcher b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public boolean matches(Topic t) {
            notNull(t, "Topic");
            return a.matches(t) || b.matches(t);
        }

        @Override
        public boolean equals(Object other) {
            return other instanceof OrMatcher
                    && ((OrMatcher) other).a.equals(a)
                    && ((OrMatcher) other).b.equals(b);
        }

        @Override
        public int hashCode() {
            return 37 * (a.hashCode() ^ b.hashCode());
        }

        @Override
        public String toString() {
            return "or(" + a + ", " + b + ")";
        }
    }
}
