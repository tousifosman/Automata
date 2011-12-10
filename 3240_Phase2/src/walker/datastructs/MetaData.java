package walker.datastructs;

class MetaData {
    private String fileName;
    private int lineNumber;
    private int startIndex;
    private int endIndex;

    public MetaData(String fileName, int lineNumber, int startIndex, int endIndex) {
        this.fileName = fileName;
        this.lineNumber = lineNumber;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    @Override
    public String toString() {
        return "<'" + fileName + "', " + lineNumber + ", " + startIndex + ", " + endIndex + ">";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final MetaData other = (MetaData) obj;
        if ((this.fileName == null) ? (other.fileName != null) : !this.fileName.equals(other.fileName))
            return false;
        if (this.lineNumber != other.lineNumber) return false;
        if (this.startIndex != other.startIndex) return false;
        if (this.endIndex != other.endIndex) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + (this.fileName != null ? this.fileName.hashCode() : 0);
        hash = 67 * hash + this.lineNumber;
        hash = 67 * hash + this.startIndex;
        hash = 67 * hash + this.endIndex;
        return hash;
    }
    
    public String getFile() {
        return fileName;
    }
}
