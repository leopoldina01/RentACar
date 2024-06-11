package dao;

import beans.Address;

import java.util.Collection;
import java.util.HashMap;

public class AddressDAO {
	private HashMap<Integer, Address> addresses = new HashMap<Integer, Address>();

    public AddressDAO() {
    }

    public Collection<Address> findAll()
    {
        return addresses.values();
    }

    public Address findAddress(int id)
    {
        return addresses.containsKey(id) ? addresses.get(id) : null;
    }

    public Address save(Address address)
    {
        Integer maxId = -1;
        for(int id : addresses.keySet()){
            int idNum = id;
            if(idNum > maxId){
                maxId = idNum;
            }
        }
        maxId++;
        address.setId(maxId);
        addresses.put(address.getId(), address);
        return address;
    }

    public Address update(int id, Address address){
        Address addressToUpdate = findAddress(id);
        if(addressToUpdate == null){
            return save(address);
        }
        addressToUpdate.setCity(address.getCity());
        addressToUpdate.setNumber(address.getNumber());
        addressToUpdate.setStreet(address.getStreet());
        addressToUpdate.setZipCode(address.getZipCode());

        delete(id);
        save(addressToUpdate);
        return addressToUpdate;
    }

    public void delete(int id)
    {
        addresses.remove(id);
    }
}
