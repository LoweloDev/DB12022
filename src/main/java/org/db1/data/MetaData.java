package org.db1.data;

/**
 * Model für die MetaDaten von Tabellen um typisierung und leichten Zugriff zu sichern.
 */

public class MetaData {
    private final String name;
    private final String typeName;
    private final int typeNo;
    private final boolean nullable;
    private final boolean primaryKey;

    /**
     * Konsumiert im Folgenden erklärte Parameter und gibt eine Instanz von <code>MetaData</code> zurück.
     * @param name Name des Metadatensatzes bspw. Spaltenname
     * @param type Datentyp bspw CHAR
     * @param typeNo Datenttypummer
     * @param nullable Gibt an ob der Datensatz nullable also optional ist
     * @param primaryKey Gibt an ob der Datensatz ein Primary Key ist
     */
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
