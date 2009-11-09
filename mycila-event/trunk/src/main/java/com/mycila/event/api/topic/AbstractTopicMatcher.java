package com.mycila.event.api.topic;

import java.io.Serializable;

import static com.mycila.event.api.util.Ensure.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public abstract class AbstractTopicMatcher implements TopicMatcher {

    public final AbstractTopicMatcher or(TopicMatcher other) {
        return new OrMatcher(this, notNull(other, "TopicMatcher"));
    }

    public final AbstractTopicMatcher and(TopicMatcher other) {
        return new AndMatcher(this, notNull(other, "TopicMatcher"));
    }

    @Override
    public abstract boolean equals(Object other);

    @Override
    public abstract int hashCode();

    @Override
    public abstract String toString();

    private static class AndMatcher extends AbstractTopicMatcher implements Serializable {
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

    private static class OrMatcher extends AbstractTopicMatcher implements Serializable {
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
