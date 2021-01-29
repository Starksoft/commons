package ru.starksoft.commons.cache;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;

import static org.junit.Assert.assertEquals;

public class CacheDiffUtilTest {

    @Test
    public void calculateDiff() {
        List<CacheEntry> oldList = new ArrayList<>();
        List<CacheEntry> newList = new ArrayList<>();

        oldList.add(new TestEntry(1, "First"));
        oldList.add(new TestEntry(2, "Second"));
        oldList.add(new TestEntry(3, "Third"));

        newList.add(new TestEntry(1, "First"));
        newList.add(new TestEntry(3, "Third-changed"));
        newList.add(new TestEntry(4, "Fourth"));

        DiffResult<CacheEntry> diffResult = CacheDiffUtil.calculateDiff(oldList, newList);

        assertEquals(1, diffResult.getToAdd().size());
        assertEquals(1, diffResult.getToRemove().size());
        assertEquals(1, diffResult.getToUpdate().size());

        // Добавили одну запись с id=4
        assertEquals("4", diffResult.getToAdd().get(0).getEntryId());
        // Удалили одну запись с id=2
        assertEquals("2", diffResult.getToRemove().get(0));
        // Изменили одну запись с id=3
        assertEquals("3", diffResult.getToUpdate().get(0).getEntryId());
    }

    @Test
    public void calculateBidDiff() {
        List<CacheEntry> oldList = populate(30_000);
        List<CacheEntry> newList = populate(30_000);

        DiffResult<CacheEntry> diffResult = CacheDiffUtil.calculateDiff(oldList, newList);

        assertEquals(0, diffResult.getToAdd().size());
        assertEquals(0, diffResult.getToRemove().size());
        assertEquals(0, diffResult.getToUpdate().size());
    }

    @Test
    public void calculateBidDiff2() {
        List<CacheEntry> oldList = populate(30_000);
        List<CacheEntry> newList = populate(29_999);

        DiffResult<CacheEntry> diffResult = CacheDiffUtil.calculateDiff(oldList, newList);

        assertEquals(0, diffResult.getToAdd().size());
        assertEquals(1, diffResult.getToRemove().size());
        assertEquals(0, diffResult.getToUpdate().size());
    }

    @Test
    public void calculateBidDiff3() {
        List<CacheEntry> oldList = populate(29_999);
        List<CacheEntry> newList = populate(30_000);

        DiffResult<CacheEntry> diffResult = CacheDiffUtil.calculateDiff(oldList, newList);

        assertEquals(1, diffResult.getToAdd().size());
        assertEquals(0, diffResult.getToRemove().size());
        assertEquals(0, diffResult.getToUpdate().size());
    }

    @Test
    public void calculateBidDiffAndCheckSpeed() {
        List<CacheEntry> oldList = populate(29_999);
        List<CacheEntry> newList = populate(30_000);

        long startTime = System.currentTimeMillis();

        DiffResult<CacheEntry> diffResult = CacheDiffUtil.calculateDiff(oldList, newList);

        long endTime = System.currentTimeMillis();

        assertEquals(1, diffResult.getToAdd().size());
        assertEquals(0, diffResult.getToRemove().size());
        assertEquals(0, diffResult.getToUpdate().size());

        //assertTrue("time taken: " + (endTime - startTime), (endTime - startTime) < 3500);
    }

    @Test
    public void calculateBidDiffAndCheckChanges() {
        List<CacheEntry> oldList = populate(30_000);
        List<CacheEntry> newList = populate(2);

        newList.add(new TestEntry(3, "new title"));

        DiffResult<CacheEntry> diffResult = CacheDiffUtil.calculateDiff(oldList, newList);

        assertEquals(0, diffResult.getToAdd().size());
        assertEquals(29997, diffResult.getToRemove().size());
        assertEquals(1, diffResult.getToUpdate().size());

        //assertTrue("time taken: " + (endTime - startTime), (endTime - startTime) < 3500);
    }

    @NonNull
    private ArrayList<CacheEntry> populate(int size) {
        ArrayList<CacheEntry> result = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            int itemNo = i + 1;
            result.add(new TestEntry(itemNo, "Test entry #" + itemNo));
        }

        return result;
    }

    static class TestEntry implements CacheEntry {

        private final int id;
        private final String title;

        TestEntry(int id, String title) {
            this.id = id;
            this.title = title;
        }

        @NonNull
        @Override
        public String getEntryId() {
            return String.valueOf(id);
        }

        @Override
        public int getContentHashCode() {
            return Objects.hash(id, title);
        }
    }
}
