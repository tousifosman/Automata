package walker.datastructs;

import java.util.HashSet;
import java.util.Set;

public class StringWithMetaData {
    private String string;
    private Set<MetaData> metaData;

    public StringWithMetaData(String string, String fileName, int lineNumber, int startIndex, int endIndex) {
        this(string, new MetaData(fileName, lineNumber, startIndex, endIndex));
    }

    public StringWithMetaData(String string, MetaData data) {
        this.metaData = new HashSet<MetaData>();
        metaData.add(data);
        this.string = string;
    }

    public StringWithMetaData(String string, Set<MetaData> metaData) {
        this.metaData = metaData;
        this.string = string;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append('"');
        builder.append(string);
        builder.append('"');
        for (MetaData data : metaData) {
            builder.append(data);
        }
        return builder.toString();
    }

    public StringWithMetaData merge(StringWithMetaData toMerge) {
        if (toMerge.string.equals(this.string)) {
            Set<MetaData> metaData = new HashSet<MetaData>();
            metaData.addAll(this.metaData);
            metaData.addAll(toMerge.metaData);
            return new StringWithMetaData(this.string, metaData);
        } else {
            return this;
        }
    }

    public StringWithMetaData remove(StringWithMetaData toRemove) {
        if (toRemove.string.equals(this.string)) {
            Set<MetaData> metaData = new HashSet<MetaData>();
            metaData.addAll(this.metaData);
            metaData.removeAll(toRemove.metaData);
            return new StringWithMetaData(this.string, metaData);
        } else {
            return this;
        }
    }

    public String getString() {
        return string;
    }

    public boolean contains(StringWithMetaData metaString) {
        if (metaString.string.equals(this.string)) {
            Set<MetaData> thisMeta = this.metaData;
            Set<MetaData> otherMeta = metaString.metaData;

            return thisMeta.containsAll(otherMeta);
        } else {
            return false;
        }
    }

    public boolean equals(Object o) {
        if (o instanceof StringWithMetaData) {
            StringWithMetaData other = (StringWithMetaData) o;
            if (other.contains(this) && this.contains(other)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    
    public int size() {
        return metaData.size();
    }
}