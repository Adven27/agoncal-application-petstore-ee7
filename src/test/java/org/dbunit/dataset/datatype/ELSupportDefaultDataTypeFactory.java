package org.dbunit.dataset.datatype;

import java.util.Date;

public class ELSupportDefaultDataTypeFactory extends DefaultDataTypeFactory {
    @Override
    public DataType createDataType(int sqlType, String sqlTypeName) throws DataTypeException {
        DataType dataType = super.createDataType(sqlType, sqlTypeName);
        if (DataType.TIMESTAMP.equals(dataType)) {
            dataType = new ELSupportTimestampType();
        }
        return dataType;
    }

    public static class ELSupportTimestampType extends TimestampDataType {
        @Override
        public Object typeCast(Object value) throws TypeCastException {
            return super.typeCast(nowCommand(value) ? new Date() : value);
        }

        private boolean nowCommand(Object value) {
            return value instanceof String && "now".equals(((String) value).trim());
        }
    }
}