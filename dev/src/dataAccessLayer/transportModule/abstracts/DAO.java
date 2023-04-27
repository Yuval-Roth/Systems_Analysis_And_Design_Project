package dataAccessLayer.transportModule.abstracts;

import dataAccessLayer.dalUtils.Cache;
import dataAccessLayer.dalUtils.DalException;
import dataAccessLayer.dalUtils.OfflineResultSet;
import dataAccessLayer.dalUtils.SQLExecutor;
import objects.transportObjects.Truck;

import java.sql.SQLException;
import java.util.List;

public abstract class DAO<T> {

    protected final SQLExecutor cursor;
    protected final String TABLE_NAME;
    protected final String[] ALL_COLUMNS;
    protected final String[] PRIMARY_KEYS;
    protected final String[] TYPES;

    protected final Cache<T> cache;

    protected DAO(String tableName,String[] types, String[] primaryKeys, String ... allColumns) throws DalException {
        this.cursor = new SQLExecutor();
        this.TABLE_NAME = tableName;
        this.PRIMARY_KEYS = primaryKeys;
        this.ALL_COLUMNS = allColumns;
        this.TYPES = types;
        this.cache = new Cache<>();
        initTable();
    }

    /**
     * used for testing
     * @param dbName the name of the database to connect to
     */
    protected DAO(String dbName, String tableName,String[] types, String[] primaryKeys, String ... allColumns) throws DalException {
        this.cursor = new SQLExecutor(dbName);
        this.TABLE_NAME = tableName;
        this.PRIMARY_KEYS = primaryKeys;
        this.ALL_COLUMNS = allColumns;
        this.TYPES = types;
        this.cache = new Cache<>();
        initTable();
    }

    /**
     * Initialize the table if it doesn't exist
     */
    protected void initTable() throws DalException{
        StringBuilder query = new StringBuilder();

        query.append(String.format("CREATE TABLE IF NOT EXISTS %s (\n", TABLE_NAME));

        for (int i = 0; i < ALL_COLUMNS.length; i++) {
            query.append(String.format("%s %s NOT NULL,\n", ALL_COLUMNS[i], TYPES[i]));
        }

        query.append("PRIMARY KEY(");
        for(int i = 0; i < PRIMARY_KEYS.length; i++) {
            query.append(String.format("'%s'",PRIMARY_KEYS[i]));
            if (i != PRIMARY_KEYS.length-1) {
                query.append(",");
            } else {
                query.append(")\n");
            }
        }

        query.append(");");

        try {
            cursor.executeWrite(query.toString());
        } catch (SQLException e) {
            throw new DalException("Failed to initialize table "+TABLE_NAME, e);
        }
    }

    /**
     * @param object getLookUpObject(identifier) of the object to select
     * @return the object with the given identifier
     * @throws DalException if an error occurred while trying to select the object
     */
    public abstract T select(T object) throws DalException;

    /**
     * @return All the objects in the table
     * @throws DalException if an error occurred while trying to select the objects
     */
    public abstract List<T> selectAll() throws DalException;

    /**
     * @param object - the object to insert
     * @throws DalException if an error occurred while trying to insert the object
     */
    public abstract void insert(T object) throws DalException;

    /**
     * @param object - the object to update
     * @throws DalException if an error occurred while trying to update the object
     */
    public abstract void update(T object) throws DalException;

    /**
     *
     * @param object getLookUpObject(identifier) of the object to delete
     * @throws DalException if an error occurred while trying to delete the object
     */
    public abstract void delete(T object) throws DalException;

    protected abstract T getObjectFromResultSet(OfflineResultSet resultSet);

    /**
     * used for testing
     */
    public void clearTable(){
        String query = String.format("DELETE FROM %s", TABLE_NAME);
        try {
            cursor.executeWrite(query);
            cache.clear();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}