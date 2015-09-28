package eu.europa.ec.fisheries.uvms.reporting.service.merger;

import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingServiceException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class Merger<I,O> {
    private class FlaggedEntry {
        private final O item;

        private boolean created;
        private boolean updated;
        private boolean deleted;

        private FlaggedEntry(final O convertedItem) {
            this.item = convertedItem;
        }
    }

    /**
     * Merge a collection of items
     *
     * @param inputs items to merge
     * @return
     * @throws ReportingServiceException
     */
    public boolean merge(final Collection<I> inputs) throws ReportingServiceException {
        Map<Object, FlaggedEntry> incommingRecords = createIncomming(inputs);
        Map<Object, FlaggedEntry> currentRecords = createCurrent(inputs, incommingRecords);

        merge(incommingRecords, currentRecords);

        return save(currentRecords);
    }

    private boolean save(final Map<Object, Merger<I, O>.FlaggedEntry> current) throws ReportingServiceException {
        boolean updated=false;
        for (Merger<I, O>.FlaggedEntry flaggedItem : current.values()) {
            if (flaggedItem.created) {
                insert(flaggedItem.item);
                updated|=true;
            } else if (flaggedItem.updated) {
                update(flaggedItem.item);
                updated|=true;
            } else if (flaggedItem.deleted) {
                delete(flaggedItem.item);
                updated|=true;
            }
        }
        return updated;
    }

    private void merge(final Map<Object, Merger<I, O>.FlaggedEntry> incomming, final Map<Object, Merger<I, O>.FlaggedEntry> current) throws ReportingServiceException {
        for (Merger<I, O>.FlaggedEntry flaggedItem : incomming.values()) {
            merge(flaggedItem, current);
        }
    }

    private void merge(final Merger<I, O>.FlaggedEntry incommingItem, final Map<Object, Merger<I, O>.FlaggedEntry> current) throws ReportingServiceException {
        Object key=getUniqKey(incommingItem.item);
        Merger<I, O>.FlaggedEntry currentItem = current.get(key);
        if (currentItem == null) {
            incommingItem.created=true;
            current.put(key, incommingItem);
        } else {
            currentItem.updated=merge(incommingItem.item, currentItem.item);
        }
    }

    private Map<Object, Merger<I, O>.FlaggedEntry> createCurrent(final Collection<I> input,
                                                                 final Map<Object, Merger<I, O>.FlaggedEntry> incomming) throws ReportingServiceException {
        Map<Object, Merger<I, O>.FlaggedEntry> current=new HashMap<>();

        for (O item: loadCurrents(input)) {
            Merger<I, O>.FlaggedEntry flaggedItem = new FlaggedEntry(item);
            Object key=getUniqKey(item);
            flaggedItem.deleted = !incomming.containsKey(key);
            current.put(key, flaggedItem);
        }

        return current;
    }

    private Map<Object, FlaggedEntry> createIncomming(final Collection<I> inputs) throws ReportingServiceException {
        Map<Object, FlaggedEntry> incomming = new HashMap<>();
        for (I input : inputs) {
            for (O convertedItem : convert(input)) {
                incomming.put(getUniqKey(convertedItem), new FlaggedEntry(convertedItem));
            }
        }
        return incomming;
    }

    /**
     * Get unique key of an item
     *
     * @param item item to check
     * @return unique key - must implement equal/hash correctly
     * @throws ReportingServiceException exception during processing
     */
    protected abstract Object getUniqKey(final O item) throws ReportingServiceException;

    /**
     * Convert an item
     *
     * @param input
     * @return resulting converted items
     * @throws ReportingServiceException exception during processing
     */
    protected abstract Collection<O> convert(final I input) throws ReportingServiceException;

    /**
     * retrieve current items
     *
     * @param input input items
     * @return items to merge
     * @throws ReportingServiceException exception during processing
     */
    protected abstract Collection<O> loadCurrents(final Collection<I> input) throws ReportingServiceException;

    /**
     * Merge 2 items
     *
     * @param incoming new item - not saved
     * @param existing existing item - will be saved
     * @return true if an update was done
     * @throws ReportingServiceException exception during processing
     */
    protected abstract boolean merge(final O incoming, final O existing) throws ReportingServiceException;


    /**
     * Insert a new record
     *
     * @param item item to insert
     * @throws ReportingServiceException exception during processing
     */
    protected abstract void insert(final O item) throws ReportingServiceException;

    /**
     * Update an existing record
     *
     * @param item
     * @throws ReportingServiceException exception during processing
     */
    protected abstract void update(final O item) throws ReportingServiceException;

    /**
     * delete an existing record
     *
     * @param item
     * @throws ReportingServiceException exception during processing
     */
    protected abstract void delete(final O item) throws ReportingServiceException;
}
