package walker.datastructs;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class StringList implements List<StringWithMetaData> {
    private Map<String, StringWithMetaData> internalMap;
    private List<StringWithMetaData> internalList;

    public StringList() {
        internalMap = new HashMap<String, StringWithMetaData>();
        internalList = new LinkedList<StringWithMetaData>();
    }

    @Override
    public int size() {
        return internalList.size();
    }

    @Override
    public boolean isEmpty() {
        return internalList.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        if (o instanceof StringWithMetaData) {
            StringWithMetaData metaString = (StringWithMetaData) o;
            String str = metaString.getString();
            if (internalMap.containsKey(str)) {
                StringWithMetaData existingString = internalMap.get(str);
                return existingString.contains(metaString);
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public Iterator<StringWithMetaData> iterator() {
        return internalList.iterator();
    }

    @Override
    public Object[] toArray() {
        return internalList.toArray();
    }

    @Override
    public <T> T[] toArray(T[] ts) {
        return internalList.toArray(ts);
    }

    @Override
    public boolean add(StringWithMetaData e) {
        String str = e.getString();

        if (internalMap.containsKey(str)) {
            StringWithMetaData existingData = internalMap.get(str);
            if (existingData.equals(e)) {
                return false;
            } else {
                StringWithMetaData mergedData = existingData.merge(e);

                internalMap.put(str, mergedData);

                internalList.remove(existingData);
                internalList.add(mergedData);
                return true;
            }
        } else {
            internalMap.put(str, e);
            internalList.add(e);
            return true;
        }
    }

    @Override
    public boolean remove(Object o) {
        String str = ((StringWithMetaData) o).getString();

        if (internalMap.containsKey(str)) {
            StringWithMetaData existingString = internalMap.get(str);
            if (existingString.equals((StringWithMetaData) o)) {
                internalMap.remove(str);
                internalList.remove(existingString);
                return true;
            } else {
                if (existingString.contains((StringWithMetaData) o)) {
                    StringWithMetaData difference = existingString.remove((StringWithMetaData) o);
                    internalMap.put(str, difference);

                    internalList.remove(existingString);
                    internalList.add(difference);
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean containsAll(Collection<?> clctn) {
        for (Object in : clctn) {
            if (!this.contains(in)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends StringWithMetaData> clctn) {
        boolean added = false;
        for (StringWithMetaData in : clctn) {
            added = this.add(in) || added;
        }
        return added;
    }

    @Override
    public boolean addAll(int i, Collection<? extends StringWithMetaData> clctn) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public boolean removeAll(Collection<?> clctn) {
        boolean removed = false;
        for (Object in : clctn) {
            removed = this.remove(in) || removed;
        }
        return removed;
    }

    @Override
    public boolean retainAll(Collection<?> clctn) {
        if (clctn instanceof StringList) {
            StringList newList = new StringList();
            newList.addAll((StringList) clctn);
            newList.addAll(this);
            
            for (StringWithMetaData data : this) {
                if (!((StringList)clctn).containsString(data)) {
                    newList.remove(data);
                }
            }
            for (StringWithMetaData data : (StringList)clctn) {
                if (!this.containsString(data)) {
                    newList.remove(data);
                }
            }
            
            this.internalList = newList.internalList;
            this.internalMap = newList.internalMap;
            
            return true;
        } else {
            throw new ClassCastException("Can only retain from StringLists");
        }
    }

    @Override
    public void clear() {
        internalMap.clear();
        internalList.clear();
    }

    @Override
    public StringWithMetaData get(int i) {
        return internalList.get(i);
    }

    @Override
    public StringWithMetaData set(int i, StringWithMetaData e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void add(int i, StringWithMetaData e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public StringWithMetaData remove(int i) {
        StringWithMetaData removed = internalList.get(i);
        internalList.remove(i);
        internalMap.remove(removed.getString());
        return removed;
    }

    @Override
    public int indexOf(Object o) {
        return internalList.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return internalList.lastIndexOf(o);
    }

    @Override
    public ListIterator<StringWithMetaData> listIterator() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ListIterator<StringWithMetaData> listIterator(int i) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<StringWithMetaData> subList(int i, int i1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public String toString() {
        return internalList.toString();
    }

    private boolean containsString(StringWithMetaData data) {
        return internalMap.containsKey(data.getString());
    }
}
