package ThreeLayerCS;

enum Input{
    Input_Empty,
    Input_Phone_Not_Number,
    Input_Right
}

public class BusinessLogic {
    private DataManager dataManager = new DataManager();

    public boolean addContact(String name, String address, String phone) {
        if (dataManager.phoneExists(phone)) {
            return false; // 如果电话号码已存在，返回false
        }
        return dataManager.addContact(name, address, phone);
    }

    public boolean updateContact(String id, String name, String address, String phone) {
        if (dataManager.phoneExists(phone)) {
            return false; // 如果电话号码已存在，返回false
        }
        return dataManager.updateContact(id, name, address, phone);    }

    public boolean deleteContact(String id) {
        return dataManager.deleteContact(id);
    }

    public String[][] getContacts(String name, String address, String phone) {
        return dataManager.getContacts(name, address, phone);
    }

    public Input validateContactInfo(String name, String address, String phone) {
        if (name.isEmpty() || address.isEmpty() || phone.isEmpty()) {
            return Input.Input_Empty;
        }
        if (!phone.matches("\\d+")) {
            return Input.Input_Phone_Not_Number;
        }
        return Input.Input_Right;
    }
}
