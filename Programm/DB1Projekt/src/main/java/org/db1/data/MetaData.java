package org.db1.data;

/**
 * Diese Klasse wrappt die benötigten Meta Daten von den verschiedenen Spalten
 */

public class MetaData {
    private final String name;
    private final String typeName;
    private final int typeNo;
    private final boolean nullable;
    private final boolean primaryKey;


    public MetaData(String name, String type, int typeNo, boolean nullable, boolean primaryKey) {
        this.name = name;
        this.typeName = type;
        this.nullable = nullable;
        this.typeNo = typeNo;
        this.primaryKey = primaryKey;
    }

    public String getName() {
        return name;
    }

    public String getTypeName() {
        return typeName;
    }

    public boolean isNullable() {
        return nullable;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public int getTypeNo() {
        return typeNo;
    }
}
