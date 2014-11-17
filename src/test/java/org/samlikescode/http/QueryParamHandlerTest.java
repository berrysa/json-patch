package org.samlikescode.http;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * //todo(sb)
 */
public class QueryParamHandlerTest {

    private static final DateTime NOW = DateTime.now();

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testHandleFilter() throws Exception {
        FakeClass survivor = FakeClass.of(5, NOW);
        ImmutableList<FakeClass> representations = ImmutableList.of(survivor, FakeClass.withId(1));
        ImmutableMultimap<String, String> queryParams = ImmutableMultimap.of("filter", "inactive");
        QueryParamHandler<FakeClass> qph = QueryParamHandler.<FakeClass>builder()
                .addFilter("inactive", fake -> fake.getEndDate().isPresent())
                .build();
        ImmutableList<FakeClass> result = qph.filter(queryParams, representations);
        assertEquals(1, result.size());
        assertEquals(survivor, result.get(0));
    }

    @Test
    public void testHandleFilter_badKey() throws Exception {
        FakeClass survivor = FakeClass.of(5, NOW);
        ImmutableList<FakeClass> representations = ImmutableList.of(survivor, FakeClass.withId(1));
        ImmutableMultimap<String, String> queryParams = ImmutableMultimap.of("fdsdkjfdf", "inactive");
        QueryParamHandler<FakeClass> qph = QueryParamHandler.<FakeClass>builder()
                .addFilter("inactive", fake -> fake.getEndDate().isPresent())
                .build();
        ImmutableList<FakeClass> result = qph.filter(queryParams, representations);
        assertEquals(representations, result);
    }

    @Test
    public void testHandleFilter_badValue() throws Exception {
        FakeClass survivor = FakeClass.of(5, NOW);
        ImmutableList<FakeClass> representations = ImmutableList.of(survivor, FakeClass.withId(1));
        ImmutableMultimap<String, String> queryParams = ImmutableMultimap.of("filter", "dfasdf");
        QueryParamHandler<FakeClass> qph = QueryParamHandler.<FakeClass>builder()
                .addFilter("inactive", fake -> fake.getEndDate().isPresent())
                .build();
        ImmutableList<FakeClass> result = qph.filter(queryParams, representations);
        assertEquals(representations, result);
    }

    @Test
    public void testHandleFilter_multipleIncomingValues() throws Exception {
        FakeClass survivor = FakeClass.of(5, NOW);
        ImmutableList<FakeClass> representations = ImmutableList.of(survivor, FakeClass.withId(1), FakeClass.of(300, NOW));
        ImmutableMultimap<String, String> queryParams = ImmutableMultimap.of("filter", "inactive", "filter", "lowIds");
        QueryParamHandler<FakeClass> qph = QueryParamHandler.<FakeClass>builder()
                .addFilter("inactive", fake -> fake.getEndDate().isPresent())
                .addFilter("lowIds", fake -> fake.getId() < 10)
                .build();
        ImmutableList<FakeClass> result = qph.filter(queryParams, representations);
        assertEquals(1, result.size());
        assertEquals(survivor, result.get(0));
    }

    @Test
    public void testHandleFilter_multipleIncomingValues_notAllHandled() throws Exception {
        FakeClass filteredOut = FakeClass.of(300, NOW);
        ImmutableList<FakeClass> representations = ImmutableList.of(FakeClass.of(5, NOW), FakeClass.withId(1), filteredOut);
        ImmutableMultimap<String, String> queryParams = ImmutableMultimap.of("filter", "inactive", "filter", "lowIds");
        QueryParamHandler<FakeClass> qph = QueryParamHandler.<FakeClass>builder()
                .addFilter("lowIds", fake -> fake.getId() < 10)
                .build();
        ImmutableList<FakeClass> result = qph.filter(queryParams, representations);
        assertEquals(2, result.size());
        assertFalse(result.contains(filteredOut));
    }

    @Test
    public void testHandleFilter_noIncomingValues() throws Exception {
        ImmutableList<FakeClass> representations = ImmutableList.of(FakeClass.of(5, NOW), FakeClass.withId(1), FakeClass.of(300, NOW));
        ImmutableMultimap<String, String> queryParams = ImmutableMultimap.of();
        QueryParamHandler<FakeClass> qph = QueryParamHandler.<FakeClass>builder()
                .addFilter("inactive", fake -> fake.getEndDate().isPresent())
                .addFilter("lowIds", fake -> fake.getId() < 10)
                .build();
        ImmutableList<FakeClass> result = qph.filter(queryParams, representations);
        assertEquals(result, representations);
    }

    @Test
    public void testConsume_singleValue() throws Exception {
        QueryParamHandler<FakeClass.Builder> qph = QueryParamHandler.<FakeClass.Builder>builder()
                .addConsumer("emailAddress", (email, builder) -> builder.addEmail(email))
                .build();
        ImmutableMultimap<String, String> queryParams = ImmutableMultimap.of("emailAddress", "1@1.com");
        FakeClass.Builder requestBuilder = FakeClass.builder();
        qph.consume(queryParams, requestBuilder);
        assertEquals(1, requestBuilder.build().getEmails().size());
    }

    @Test
    public void testConsume_multipleValues() throws Exception {
        QueryParamHandler<FakeClass.Builder> qph = QueryParamHandler.<FakeClass.Builder>builder()
                .addConsumer("emailAddress", (email, builder) -> builder.addEmail(email))
                .build();
        ImmutableMultimap<String, String> queryParams = ImmutableMultimap.of("emailAddress", "1@1.com", "emailAddress", "2@2.com");
        FakeClass.Builder requestBuilder = FakeClass.builder();
        qph.consume(queryParams, requestBuilder);
        assertEquals(2, requestBuilder.build().getEmails().size());
    }

    @Test
    public void testConsume_multipleConsumers() throws Exception {
        QueryParamHandler<FakeClass.Builder> qph = QueryParamHandler.<FakeClass.Builder>builder()
                .addConsumer("emailAddress", (email, builder) -> builder.addEmail(email))
                .addConsumer("id", (id, builder) -> builder.id(Integer.parseInt(id)))
                .addConsumer("endDate", (endDate, builder) -> builder.endDate(new DateTime(Long.parseLong(endDate))))
                .build();
        ImmutableMultimap<String, String> queryParams = ImmutableMultimap.<String, String>builder()
                .put("emailAddress", "1@1.com")
                .put("emailAddress", "2@2.com")
                .put("id", "54")
                .put("endDate", "32432")
                .build();
        FakeClass.Builder requestBuilder = FakeClass.builder();
        qph.consume(queryParams, requestBuilder);
        FakeClass result = requestBuilder.build();
        assertEquals(2, result.getEmails().size());
        assertEquals(54, result.getId());
        assertTrue(result.getEndDate().isPresent());
    }

    @Test
    public void testConsume_lessConsumersThenIncoming() throws Exception {
        QueryParamHandler<FakeClass.Builder> qph = QueryParamHandler.<FakeClass.Builder>builder()
                .addConsumer("emailAddress", (email, builder) -> builder.addEmail(email))
                .build();
        ImmutableMultimap<String, String> queryParams = ImmutableMultimap.<String, String>builder()
                .put("emailAddress", "1@1.com")
                .put("emailAddress", "2@2.com")
                .put("id", "54")
                .put("endDate", "32432")
                .build();
        FakeClass.Builder requestBuilder = FakeClass.builder();
        qph.consume(queryParams, requestBuilder);
        FakeClass result = requestBuilder.build();
        assertEquals(2, result.getEmails().size());
        assertNotEquals(54, result.getId());
        assertFalse(result.getEndDate().isPresent());
    }

    @Test
    public void testConsume_lessIncomingThenConsumers() throws Exception {
        QueryParamHandler<FakeClass.Builder> qph = QueryParamHandler.<FakeClass.Builder>builder()
                .addConsumer("emailAddress", (email, builder) -> builder.addEmail(email))
                .addConsumer("id", (id, builder) -> builder.id(Integer.parseInt(id)))
                .addConsumer("endDate", (endDate, builder) -> builder.endDate(new DateTime(Long.parseLong(endDate))))
                .build();
        ImmutableMultimap<String, String> queryParams = ImmutableMultimap.<String, String>builder()
                .put("id", "54")
                .put("endDate", "32432")
                .build();
        FakeClass.Builder requestBuilder = FakeClass.builder();
        qph.consume(queryParams, requestBuilder);
        FakeClass result = requestBuilder.build();
        assertEquals(54, result.getId());
        assertTrue(result.getEndDate().isPresent());
        assertTrue(result.getEmails().isEmpty());
    }

    private static class FakeClass {
        private final int id;
        private final Optional<DateTime> endDate;
        private final ImmutableList<String> emails;

        private FakeClass(int id, DateTime endDate, ImmutableList<String> emails) {
            this.id = id;
            this.endDate = Optional.ofNullable(endDate);
            this.emails = emails;
        }

        private static FakeClass of(int id, DateTime endDate, ImmutableList<String> emails) {
            return new FakeClass(id, endDate, emails);
        }

        private static FakeClass of(int id, DateTime endDate) {
            return of(id, endDate, null);
        }

        private static FakeClass withId(int id) {
            return of(id, null);
        }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {
            private final ImmutableList.Builder<String> emails = ImmutableList.builder();
            private int id;
            private DateTime endDate;

            public Builder id(int id) {
                this.id = id;
                return this;
            }

            public Builder endDate(DateTime endDate) {
                this.endDate = endDate;
                return this;
            }

            public Builder addEmail(String email) {
                emails.add(email);
                return this;
            }

            public FakeClass build() {
                return FakeClass.of(id, endDate, emails.build());
            }
        }

        private int getId() {
            return id;
        }

        private Optional<DateTime> getEndDate() {
            return endDate;
        }

        private ImmutableList<String> getEmails() {
            return emails;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            FakeClass that = (FakeClass) o;
            return Objects.equal(this.id, that.id)
                    && Objects.equal(this.endDate, that.endDate);
        }
    }
}
