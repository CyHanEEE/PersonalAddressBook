package TwoLayerCS;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataManager {
    private Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/softwarearchitecture?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC", "root", "204615");
        } catch (SQLException e) {
            throw new RuntimeException("数据库连接失败", e);
        }
        return conn;
    }

    // 增加
    public boolean addContact(String name, String address, String phone) {
        if(phoneExists(phone)) return false;
        String sql = "INSERT INTO contact (Name, Address, Phone) VALUES (?, ?, ?)";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, address);
            stmt.setString(3, phone);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new RuntimeException("添加联系人失败", e);
        }
    }

    public boolean updateContact(String id, String name, String address, String phone) {
        if(phoneExists(phone)) return false;
        String sql = "UPDATE contact SET Name = ?, Address = ?, Phone = ? WHERE Id = ?";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, address);
            stmt.setString(3, phone);
            stmt.setString(4, id);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new RuntimeException("更新联系人失败", e);
        }
    }

    public boolean deleteContact(String id) {
        String sql = "DELETE FROM contact WHERE Id = ?";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new RuntimeException("删除联系人失败", e);
        }
    }

    public String[][] getContacts(String name, String address, String phone) {
        StringBuilder sql = new StringBuilder("SELECT * FROM contact WHERE 1=1");
        List<String> params = new ArrayList<>();

        if (name != null && !name.isEmpty()) {
            sql.append(" AND Name LIKE ?");
            params.add("%" + name + "%");
        }
        if (address != null && !address.isEmpty()) {
            sql.append(" AND Address LIKE ?");
            params.add("%" + address + "%");
        }
        if (phone != null && !phone.isEmpty()) {
//            sql.append(" AND Phone = ?");
//            params.add(phone);
            sql.append(" AND Phone LIKE ?");
            params.add("%" + phone + "%");
        }


        try (
                Connection conn = connect();
                PreparedStatement stmt = conn.prepareStatement(sql.toString())

        ) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setString(i + 1, params.get(i));
            }
            ResultSet resultSet = stmt.executeQuery();
            int rowCount = 0;
            while (resultSet.next()) {
                rowCount++;
            }
            String[][] data = new String[rowCount][];
            resultSet.beforeFirst();
            int row = 0;
            while (resultSet.next()) {
                data[row] = new String[]{resultSet.getString("Id"), resultSet.getString("Name"), resultSet.getString("Address"), resultSet.getString("Phone"), "修改", "删除"};
                row++;
            }
            return data;
        } catch (SQLException e) {
            throw new RuntimeException("获取联系人列表失败", e);
        }
    }

    public boolean phoneExists(String phone) {
        String sql = "SELECT COUNT(*) FROM contact WHERE Phone = ?";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, phone);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0; // 如果存在，返回true
            }
        } catch (SQLException e) {
            throw new RuntimeException("检查电话号码失败", e);
        }
        return false; // 如果不存在，返回false
    }
}
