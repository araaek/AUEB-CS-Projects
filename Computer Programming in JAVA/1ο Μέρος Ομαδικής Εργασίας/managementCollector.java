
import java.util.*;
class managementCollector{
    //TreeMaps
    TreeMap<String , ElectronicsStore> AvailableDivices = new TreeMap<>();
    TreeMap<String, Integer> AvailableDivicesAmount = new TreeMap<>();
    TreeMap<String, Integer> OrdersListPos = new TreeMap<>();
    TreeMap<String , Integer> CouponDiscount = new TreeMap<>();
    
    int i = 0;
    //ArrayList
    ArrayList<String> salesList = new ArrayList<>(); 
    ArrayList<Order> OrdersList = new ArrayList<>();

    //voids
    void ListOfAD(String modelname, ElectronicsStore Divice , Integer diviceAmount){
    
        AvailableDivices.put(modelname , Divice);
        AvailableDivicesAmount.put(modelname , diviceAmount);
           
    }
    void  ListOfSales(String salesCode, ElectronicsStore deviceModel, String customerName, String customerphoneNumber,
                        Date saleDate, double finalCost){

        
        Sale sales= new Sale(salesCode, deviceModel, customerName, customerphoneNumber, saleDate, finalCost);


        salesList.add("-------------------------\n"+sales.toString()+"\n-------------------------\n");

    }
    void ListOfOrders(String orderCode, ElectronicsStore deviceModel, String customerName, String customerphoneNumber, 
                            Date orderDate, Date expectedarrivalDate, double finalCost, String orderStatus ){


        Order orders = new Order(orderCode, deviceModel, customerName, customerphoneNumber, orderDate, expectedarrivalDate, finalCost, orderStatus);

        OrdersList.add(orders);
        
        OrdersListPos.put(orders.getOrderCode(),i);
        i++;
       
    }   
    void OrderToSale(Order order){

        Sale sale = new Sale(order.getOrderCode(), order.getDeviceModel(), order.getCustomerName(), order.getCustomerPhoneNumber(), order.getOrderDate(), order.getFinalCost());
        salesList.add("-------------------------\n"+sale.toString()+"\n-------------------------\n");

    }    
    void listOfCoupons(){
        //εκχωρεί στο treemap την random λέξει με ένα τυχαίω ποσό      
        for(int i = 0;i < 10; i++){

            CouponDiscount.put(randomChar(), randomInt());
        }
    }    
 
 
    //else
    Integer returnPos(String orderCode){

        return OrdersListPos.get(orderCode);
    }
    String randomChar(){

        //φτίαχνει μία λέξη απο random χαρακτήρες με μήκος λέξεις 5

        final String character = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        final int  lenght = 5;

        Random random = new Random();
        StringBuilder sb = new StringBuilder(lenght);

        for(int i = 0; i<lenght; i++){

            int randomIndex = random.nextInt(character.length());

            char randomCharacter = character.charAt(randomIndex);

            sb.append(randomCharacter);

        }

        return sb.toString();
    }
    Integer randomInt(){
        //φτείαχνει το τυχαίο ποσό
        Random random = new Random();

        int startfrom = 10;
        int endfrom = 50;
        int randomNumber = random.nextInt(endfrom - startfrom +1) +startfrom;

        return randomNumber;

    }
    
    
}
