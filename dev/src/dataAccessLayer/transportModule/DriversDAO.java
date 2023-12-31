package dataAccessLayer.transportModule;

import dataAccessLayer.dalAbstracts.DAOBase;
import dataAccessLayer.dalAbstracts.SQLExecutor;
import dataAccessLayer.dalUtils.CreateTableQueryBuilder;
import dataAccessLayer.dalUtils.OfflineResultSet;
import dataAccessLayer.employeeModule.EmployeeDAO;
import domainObjects.transportModule.Driver;
import exceptions.DalException;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import static dataAccessLayer.dalUtils.CreateTableQueryBuilder.ColumnModifier;
import static dataAccessLayer.dalUtils.CreateTableQueryBuilder.ColumnType;

public class DriversDAO extends DAOBase<Driver> {

    public static final String tableName = "truck_drivers";
    public static final String primaryKey = "id";
    private static final String PARENT_TABLE_NAME = EmployeeDAO.tableName;

    public DriversDAO(SQLExecutor cursor) throws DalException {
        super(cursor, tableName);
    }

    /**
     * Used to insert data into {@link DAOBase#createTableQueryBuilder}. <br/>
     * in order to add columns and foreign keys to the table use:<br/><br/>
     * {@link CreateTableQueryBuilder#addColumn(String, ColumnType, ColumnModifier...)} <br/><br/>
     * {@link CreateTableQueryBuilder#addForeignKey(String, String, String)}<br/><br/>
     * {@link CreateTableQueryBuilder#addCompositeForeignKey(String[], String, String[])}
     */
    @Override
    protected void initializeCreateTableQueryBuilder() {
        createTableQueryBuilder
                .addColumn("id", ColumnType.TEXT, ColumnModifier.PRIMARY_KEY)
                .addColumn("license_type", ColumnType.TEXT, ColumnModifier.NOT_NULL)
                .addForeignKey("id", EmployeeDAO.tableName, EmployeeDAO.primaryKey);
    }

    /**
     * @param object@return the object with the given identifier
     * @throws DalException if an error occurred while trying to select the object
     */
    @Override
    public Driver select(Driver object) throws DalException {

        if(cache.contains(object)) {
            return cache.get(object);
        }

        String query = String.format("""
                    SELECT %s.id,%s.name,license_type FROM %s
                    INNER JOIN %s ON %s.id = %s.id
                    WHERE %s.id = '%s';
                        """,
            TABLE_NAME, PARENT_TABLE_NAME, TABLE_NAME, PARENT_TABLE_NAME, TABLE_NAME,
                PARENT_TABLE_NAME, TABLE_NAME, object.id());
        OfflineResultSet resultSet;
        try {
            resultSet = cursor.executeRead(query);
        } catch (SQLException e) {
            throw new DalException("Failed to select Driver", e);
        }
        if (resultSet.next()) {
            Driver selected = getObjectFromResultSet(resultSet);
            cache.put(selected);
            return selected;
        } else {
            throw new DalException("No driver with id " + object.id() + " was found");
        }
    }

    /**
     * @return All the objects in the table
     * @throws DalException if an error occurred while trying to select the objects
     */
    @Override
    public List<Driver> selectAll() throws DalException {
        String query = String.format("""
                    SELECT %s.id,%s.name,license_type FROM %s
                    INNER JOIN %s ON %s.id = %s.id;
                        """,
            TABLE_NAME,PARENT_TABLE_NAME, TABLE_NAME, PARENT_TABLE_NAME, TABLE_NAME, PARENT_TABLE_NAME);
        OfflineResultSet resultSet;
        try {
            resultSet = cursor.executeRead(query);
        } catch (SQLException e) {
            throw new DalException("Failed to select all Drivers", e);
        }
        List<Driver> drivers = new LinkedList<>();
        while (resultSet.next()) {
            drivers.add(getObjectFromResultSet(resultSet));
        }
        cache.putAll(drivers);
        return drivers;
    }

    /**
     * @param object - the object to insert
     * @throws DalException if an error occurred while trying to insert the object
     */
    @Override
    public void insert(Driver object) throws DalException {
        String query = String.format("INSERT INTO %s VALUES ('%s', '%s');",
                TABLE_NAME,
                object.id(),
                object.licenseType());
        try {
            if(cursor.executeWrite(query) == 1){
                cache.put(object);
            } else {
                throw new RuntimeException("Unexpected error while inserting driver");
            }
        } catch (SQLException e) {
            throw new DalException("Failed to insert Driver", e);
        }
    }

    /**
     * @param object - the object to update
     * @throws DalException if an error occurred while trying to update the object
     */
    @Override
    public void update(Driver object) throws DalException {
        String query = String.format("UPDATE %s SET license_type = '%s' WHERE id = '%s';",
                TABLE_NAME,
                object.licenseType(),
                object.id());
        try {
            if(cursor.executeWrite(query) == 1){
                cache.put(object);
            } else {
                throw new DalException("No driver with id " + object.id() + " was found");
            }
        } catch (SQLException e) {
            throw new DalException("Failed to update Driver", e);
        }
    }

    /**
     * @param object@throws DalException if an error occurred while trying to delete the object
     */
    @Override
    public void delete(Driver object) throws DalException {
        String query = String.format("DELETE FROM %s WHERE id = '%s';", TABLE_NAME, object.id());
        try {
            if(cursor.executeWrite(query) == 1){
                cache.remove(object);
            } else {
                throw new DalException("No driver with id " + object.id() + " was found");
            }
        } catch (SQLException e) {
            throw new DalException("Failed to delete Driver", e);
        }
    }

    @Override
    public boolean exists(Driver object) throws DalException {

        if(cache.contains(object)) {
            return true;
        }

        String query = String.format("SELECT * FROM %s WHERE id = '%s';", TABLE_NAME, object.id());
        OfflineResultSet resultSet;
        try {
            resultSet = cursor.executeRead(query);
            if(resultSet.next()) {
                Driver selected = getObjectFromResultSet(resultSet);
                cache.put(selected);
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            throw new DalException("Failed to check if Driver exists", e);
        }
    }

    @Override
    protected Driver getObjectFromResultSet(OfflineResultSet resultSet) {
        return new Driver(
                resultSet.getString("id"),
                resultSet.getString("name"),
                Driver.LicenseType.valueOf(resultSet.getString("license_type")));
    }
}
