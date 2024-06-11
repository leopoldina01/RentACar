package dao;

import beans.CustomerType;

import java.util.Collection;
import java.util.HashMap;

public class CustomerTypeDAO {
	private HashMap<Integer, CustomerType> customerTypes = new HashMap<Integer, CustomerType>();

    public CustomerTypeDAO() {}

    public Collection<CustomerType> findAll()
    {
        return customerTypes.values();
    }

    public CustomerType findCustomerType(int id)
    {
        return customerTypes.containsKey(id) ? customerTypes.get(id) : null;
    }

    public CustomerType save(CustomerType customerType){
        Integer maxId = -1;
        for(int id : customerTypes.keySet()){
            int idNum = id;
            if(idNum > maxId){
                maxId = idNum;
            }
        }
        maxId++;
        customerType.setId(maxId);
        customerTypes.put(customerType.getId(), customerType);
        return customerType;
    }

    public CustomerType update(int id, CustomerType customerType)
    {
        CustomerType customerTypeToUpdate = findCustomerType(id);

        if (customerTypeToUpdate == null)
        {
            return save(customerType);
        }

        customerTypeToUpdate.setType(customerType.getType());
        customerTypeToUpdate.setDiscount(customerType.getDiscount());
        customerTypeToUpdate.setRequiredScore(customerType.getRequiredScore());

        //delete(id);

        //save(customerTypeToUpdate);

        return customerTypeToUpdate;
    }

    public void delete(int id)
    {
        customerTypes.remove(id);
    }
}
