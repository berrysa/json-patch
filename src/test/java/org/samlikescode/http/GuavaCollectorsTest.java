package org.samlikescode.http;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.samlikescode.http.GuavaCollectors.toImmutableList;
import static org.samlikescode.http.GuavaCollectors.toImmutableMap;
import static org.samlikescode.http.GuavaCollectors.toImmutableSet;
import static org.samlikescode.http.GuavaCollectors.toNaturalImmutableSortedSet;

/**
 * Copied from https://github.com/maciejmiklas/cyclop/blob/master/cyclop-webapp/src/test/java/org/cyclop/common/TestGullectors.java
 */
public class GuavaCollectorsTest {

    @Test
    public void testToNaturalImmutableSortedSet_Sort() {
        List<String> vals = Arrays.asList("charlie", "bravo", "delta", "india", "echo", "alpha");
        ImmutableSortedSet<String> res = vals.stream().collect(toNaturalImmutableSortedSet());
        assertEquals("[alpha, bravo, charlie, delta, echo, india]", res.toString());
    }

    @Test
    public void testToNaturalImmutableSortedSet_Parallel() {
        List<String> vals = Arrays.asList("charlie", "bravo", "delta", "india", "echo", "alpha");
        ImmutableSortedSet<String> res = vals.parallelStream().collect(toNaturalImmutableSortedSet());
        assertEquals("[alpha, bravo, charlie, delta, echo, india]", res.toString());
    }

    @Test
    public void testToNaturalImmutableSortedSet_Empty() {
        List<String> vals = Arrays.asList();
        ImmutableSortedSet<String> res = vals.stream().collect(toNaturalImmutableSortedSet());
        assertTrue(res.isEmpty());
    }

    @Test
    public void testToImmutableSet_Sort() {
        List<Integer> vals = Arrays.asList(5, 1, 22, 4, 8, 9, 3, 3, 989);
        ImmutableSet<Integer> res = vals.stream().collect(toImmutableSet());
        assertEquals("[5, 1, 22, 4, 8, 9, 3, 989]", res.toString());
    }

    @Test
    public void testToImmutableSet_Parallel() {
        List<Integer> vals = Arrays.asList(5, 1, 22, 4, 8, 9, 3, 3, 989);
        ImmutableSet<Integer> res = vals.parallelStream().collect(toImmutableSet());
        assertEquals("[5, 1, 22, 4, 8, 9, 3, 989]", res.toString());
    }

    @Test
    public void testToImmutableSet_Empty() {
        List<String> vals = Arrays.asList();
        ImmutableSet<String> res = vals.stream().collect(toImmutableSet());
        assertTrue(res.isEmpty());
    }

    @Test
    public void testToImmutableList_Sort() {
        List<Integer> vals = Arrays.asList(1, 3, 4, 5, 8, 9, 3, 989);
        ImmutableList<Integer> res = vals.stream().collect(toImmutableList());
        assertEquals("[1, 3, 4, 5, 8, 9, 3, 989]", res.toString());
    }

    @Test
    public void testToImmutableList_Parallel() {
        List<Integer> vals = Arrays.asList(1, 3, 4, 5, 8, 9, 3, 989);
        ImmutableList<Integer> res = vals.parallelStream().collect(toImmutableList());
        assertEquals("[1, 3, 4, 5, 8, 9, 3, 989]", res.toString());
    }

    @Test
    public void testToImmutableMap_Sort() {
        List<Data> vals = Arrays.asList(new Data(22, "a"), new Data(11, "bb"), new Data(231, "cc"),
                new Data(1231, "cc"));
        ImmutableMap<Integer, String> res = vals.stream().collect(toImmutableMap(d -> d.ii, d -> d.ss));
        assertEquals("{22=a, 11=bb, 231=cc, 1231=cc}", res.toString());
    }

    @Test
    public void testToImmutableMap_Empty() {
        List<Data> vals = Arrays.asList();
        ImmutableMap<Integer, String> res = vals.stream().collect(toImmutableMap(d -> d.ii, d -> d.ss));
        assertTrue(res.isEmpty());
    }

    static class Data {
        Integer ii;
        String ss;

        Data(Integer ii, String ss) {
            this.ii = ii;
            this.ss = ss;
        }
    }
}