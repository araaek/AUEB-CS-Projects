import java.util.*;
import java.util.Date;
import java.text.SimpleDateFormat;
class MainProgram {
    
    
    public static void main(String[] args) throws Exception{
        
        
        managementCollector MC = new managementCollector();

        MC.listOfCoupons();
        
        Camera cameraDivice1 = new Camera("x4t79", "f12-max-plus-White", 2017, "fony", 25.60, "DSLR", 9, 4, 8, 3.1);
        Camera cameraDivice2 = new Camera("750erw2", "en-mirrorless-II-Black", 2018, "enipeas", 60.27, "Mirrorless", 16, 5, 10, 3.5);
        Console consoleDevice1 = new Console("Qrt24Gh5", "Flaystation-5", 2020, "fony", 600, "Home video game console", "ANB ZEN 3 CPU", "RGX 2060", "LInear PCM (audio format)", 1500);
        Console consoleDevice2 = new Console("qw43trg", "XcanisterS", 2020, "macrohard", 400, "Home video game console", "indal ZEN 3 CPU", "RGX 2040", "LInear PCM (audio format)", 1000);
        Refrigerator refrigeratorD1 = new Refrigerator("aw235frt", "MAXSUNG-Smart-Iverter-Compressor-WIFI", 2017, "Maxsung", 800, "side-by-side", "190 watts", 550, 140);
        Refrigerator refrigeratorD2 = new Refrigerator("sw5387g", "LJ-RS4000-Fridge-Freezer", 2020, "LJ", 1000, "French door", "200 watts", 590, 200);
        Television tvdevice1 = new Television("qw23ew23", "Fony-Curved-4k",2019, "Fony", 890, "curved", "size: 40 inches, width: 88.6cm, height: 49.8cm " , "3840 x 2160 pixels", "3 HDMI, 1 LAN, 2 Optical Digital");
        Television tvdevice2 = new Television("qw45trda", "LJ-Flat-1080p-(Full HD)", 2020, "LJ", 400, "flat", "size: 43 inches, width: 95.3cm, height: 53.6cm", "1,920 x 1,080 pixels", "3 HDMI, 1 LAN");
        WashingMachine washingMach1 = new WashingMachine("ty56upa", "LJ-9kg-1400-revolution-WIFI-White-Inox", 2016, "LJ", 700, "500 watts", 9, 1400);
        WashingMachine washingMach2 = new WashingMachine("12rteas5", "Maxsung-6kg-100-revolution-WIFI-Black-Inox", 2022, "Maxsung", 500, "800 watts", 6, 1000);

        
        //δείτε το diviceAmount ή τα σχόλια απο δίπλα λένε πόσα είναι το καθένα
        MC.ListOfAD("f12-max-plus-White", cameraDivice1, 5);// 5
        MC.ListOfAD("en-mirrorless-II-Black", cameraDivice2,2);// 2
        MC.ListOfAD("Flaystation-5", consoleDevice1,14);// 14
        MC.ListOfAD("XcanisterS", consoleDevice2, 0);// 0
        MC.ListOfAD("MAXSUNG-Smart-Iverter-Compressor-WIFI", refrigeratorD1, 9);// 9
        MC.ListOfAD("LJ-RS4000-Fridge-Freezer", refrigeratorD2,5);// 5
        MC.ListOfAD("Fony-Curved-4k", tvdevice1, 1); // 1 
        MC.ListOfAD("LJ-Flat-1080p-(Full HD)", tvdevice2, 7);// 7
        MC.ListOfAD("LJ-9kg-1400-revolution-WIFI-White-Inox", washingMach1,10);// 10
        MC.ListOfAD("Maxsung-6kg-100-revolution-WIFI-Black-Inox", washingMach2, 1);// 1

        
        
        //coupons print with code and value discount
        System.out.println("\t\t\t\t\t*COUPONS*\n----------------------------------------------------------------------------------------------------\n"
                            +MC.CouponDiscount+"\n----------------------------------------------------------------------------------------------------\n");


        //main program 
        Scanner sc = new Scanner(System.in);
        System.out.println("0) Device overview\n1) Orders overview\n2) Sales overview\n3) Exit");
        int menuValue = sc.nextInt();
        while(menuValue != 3){

            if(menuValue == 0){
                int devicesMenuValue = -1;
                System.out.println("0)sound , image and gaming\n1)home appliances\n2)HomePage");
                int miniMenuValue = sc.nextInt();

                if(miniMenuValue == 0){
                    System.out.println("0)Camera\n1)TV\n2)console\n3)HomePage");
                    devicesMenuValue = sc.nextInt();
                    if(devicesMenuValue == 0){ 
                        System.out.println("1)f12-max-plus-White\n2)en-mirrorless-II-Black\n3)HomePage");
                        int  choise = sc.nextInt();
                        Camera ModelName;
                        String modelNamesString;


                        if(choise == 1){

                            modelNamesString = "f12-max-plus-White";
                            ModelName = cameraDivice1;

                        }
                        else if(choise == 2){

                            ModelName = cameraDivice2;
                            modelNamesString = "en-mirrorless-II-Black";


                        }
                        else{

                            ModelName = null;
                            modelNamesString = "";
                        }
                        if(ModelName != null){

                            System.out.println(ModelName);
                            System.out.println(ModelName.printInfo());


                            if(MC.AvailableDivicesAmount.get(modelNamesString) != 0){

                                System.out.println("1)Buy\n2)HomePage");
                                int choice = sc.nextInt();
                                if(choice == 1){
                                    System.out.println("Type the sale code: ");
                                    String codeSale = sc.next();
                                    
                                    System.out.println("Customer's name: ");
                                    String nameCustomer = sc.next();
    
                                    System.out.println("Customer's phone number: ");
                                    String phoneNumberCustomer = sc.next();
    
                                    System.out.println("Date of sale(dd-mm-yyyy): ");
                                    String dateSale  = sc.next();

                                    Double finalCost = ModelName.getPriceModel();
                                     
                                    Date SaleDate = new SimpleDateFormat("dd-MM-yyyy").parse(dateSale);

                                    System.out.println("Use coupon Discount\n1)yah\n2)nah: ");
                                    
                                        //!!  coupondiscount  !!

                                    int AvailableCoupon  = sc.nextInt();
                                    int trying = 0;
                                     
                                    while((AvailableCoupon != 1)&&(trying <3)){
                                        
                                        System.out.println("Type the code of discount coupon("+(3-trying)+" tries): ");
                                        String codeCoupon  = sc.next();
                                        if(MC.CouponDiscount.containsKey(codeCoupon)){

                                            finalCost = ModelName.getPriceModel();
                                            int value = MC.CouponDiscount.get(codeCoupon);
                                            Double doublevalue = (double) value;
                                            finalCost = finalCost - (doublevalue/100.0)*finalCost;

                                        }
                                        else{

                                            System.out.println("Wrong code");
                                            
                                        }
                                        trying++;

                                    }
                                    if(finalCost == MC.AvailableDivices.get(modelNamesString).getPriceModel()){

                                        System.out.println("Do you want to continue the order?\n1)yes\n2)no");
                                        int finalDec = sc.nextInt();
                                        if( finalDec == 1 ){

                                            MC.ListOfSales(codeSale, ModelName, phoneNumberCustomer, nameCustomer, SaleDate, finalCost);
                                            int prevAmount = MC.AvailableDivicesAmount.get(modelNamesString)-1;
                                            MC.AvailableDivicesAmount.put(modelNamesString , prevAmount);

                                        }
                                        else{

                                            System.out.println("Loading...");

                                        }

                                    }
                                    else{

                                        MC.ListOfSales(codeSale, ModelName, phoneNumberCustomer, nameCustomer, SaleDate, finalCost);
                                        int prevAmount = MC.AvailableDivicesAmount.get(modelNamesString)-1;
                                        MC.AvailableDivicesAmount.put(modelNamesString , prevAmount);

                                    }      
    
                                }
                                else{

                                    System.out.println("Loading...");

                                }
    
                                }
                                else{
        
                                    System.out.println("Not available");
                                    System.out.println("0)Order\n1)HomePage");
                                    int choice = sc.nextInt();
                                    if(choice == 0){

                                        System.out.println("Type the order code: ");
                                        String codeOrder = sc.next();
                                        
                                        System.out.println("Customer's name: ");
                                        String nameCustomer = sc.next();
        
                                        System.out.println("Customer's phone number: ");
                                        String phoneNumberCustomer = sc.next();
        
                                        System.out.println("Date of order(dd-mm-yyyy): ");
                                        String dateOrder  = sc.next();
                                        
                                        System.out.println("Redemption date receipts(dd-mm-yyyy): ");
                                        String dateExpectOrder  = sc.next();
                                        
      
                                        String orderStatus  = "Processing";
                                        Date OrderDate = new SimpleDateFormat("dd-MM-yyyy").parse(dateOrder);
                                        Date OrderDateExpected = new SimpleDateFormat("dd-MM-yyyy").parse(dateExpectOrder);

                                        System.out.println("Use coupon Discount\n1)yah\n2)nah: ");

                                        int AvailableCoupon  = sc.nextInt();
                                        int trying = 0;

                                        Double finalCost = ModelName.getPriceModel();

                                        while((AvailableCoupon != 2)&&(trying <3)){
                                        
                                            System.out.println("Type the code of discount coupon("+(3-trying)+" tries): ");
                                            String codeCoupon  = sc.next();
                                            if(MC.CouponDiscount.containsKey(codeCoupon)){
    
                                                finalCost = ModelName.getPriceModel();
                                                int value = MC.CouponDiscount.get(codeCoupon);
                                                Double doublevalue = (double) value;
                                                finalCost = finalCost - (doublevalue/100.0)*finalCost;
    
                                            }
                                            else{
    
                                                System.out.println("Wrong code");
                 
                                            }
                                            trying++;
    
                                        }
                                        if(finalCost == ModelName.getPriceModel()){
    
                                            System.out.println("Do you want to continue the order?\n1)yes\n2)no");
                                            int finalDec = sc.nextInt();
                                            if( finalDec == 1 ){   

                                                MC.ListOfOrders(codeOrder, ModelName, phoneNumberCustomer, nameCustomer, OrderDate, OrderDateExpected, finalCost, orderStatus);
                                            
                                            }
                                            else{
                                                System.out.println("Loading...");
                                            }
                                        }
                                        else{

                                            MC.ListOfOrders(codeOrder, ModelName, phoneNumberCustomer, nameCustomer, OrderDate, OrderDateExpected, finalCost, orderStatus);
                                       
                                        }
 
                                    }
                                    else{

                                        System.out.println("Loading...");
    
                                    }
 
                                }

                        }
                        else{

                            System.out.println("Loading...");

                        }

                    }
                    else if(devicesMenuValue == 1){

                        System.out.println("1)Fony-Curved-4k\n2)LJ-Flat-1080p-(Full HD)\n3)HomePage");
                        int  choise = sc.nextInt();
                        Television ModelName;
                        String modelNamesString;


                        if(choise == 1){

                            modelNamesString = "Fony-Curved-4k";
                            ModelName = tvdevice1;

                        }
                        else if(choise == 2){

                            ModelName = tvdevice2;
                            modelNamesString = "LJ-Flat-1080p-(Full HD)";


                        }
                        else{

                            ModelName = null;
                            modelNamesString = "";
                        }
                        if(ModelName != null){

                            System.out.println(ModelName);
                            System.out.println(ModelName.printInfo());


                            if(MC.AvailableDivicesAmount.get(modelNamesString) != 0){

                                System.out.println("1)Buy\n2)HomePage");
                                int choice = sc.nextInt();
                                if(choice == 1){
                                    System.out.println("Type the sale code: ");
                                    String codeSale = sc.next();
                                    
                                    System.out.println("Customer's name: ");
                                    String nameCustomer = sc.next();
    
                                    System.out.println("Customer's phone number: ");
                                    String phoneNumberCustomer = sc.next();
    
                                    System.out.println("Date of sale(dd-mm-yyyy): ");
                                    String dateSale  = sc.next();

                                    Double finalCost = ModelName.getPriceModel();
                                     
                                    Date SaleDate = new SimpleDateFormat("dd-MM-yyyy").parse(dateSale);

                                    System.out.println("Use coupon Discount\n1)yah\n2)nah: ");
                                    
                                        //!!  coupondiscount  !!

                                    int AvailableCoupon  = sc.nextInt();
                                    int trying = 0;
                                     
                                    while((AvailableCoupon != 2)&&(trying <3)){
                                        
                                        System.out.println("Type the code of discount coupon("+(3-trying)+" tries): ");
                                        String codeCoupon  = sc.next();
                                        if(MC.CouponDiscount.containsKey(codeCoupon)){

                                            finalCost = ModelName.getPriceModel();
                                            int value = MC.CouponDiscount.get(codeCoupon);
                                            Double doublevalue = (double) value;
                                            finalCost = finalCost - (doublevalue/100.0)*finalCost;

                                        }
                                        else{

                                            System.out.println("Wrong code");
                                            
                                        }
                                        trying++;

                                    }
                                    if(finalCost == MC.AvailableDivices.get(modelNamesString).getPriceModel()){

                                        System.out.println("Do you want to continue the order?\n1)yes\n2)no");
                                        int finalDec = sc.nextInt();
                                        if( finalDec == 1 ){

                                            MC.ListOfSales(codeSale, ModelName, phoneNumberCustomer, nameCustomer, SaleDate, finalCost);
                                            int prevAmount = MC.AvailableDivicesAmount.get(modelNamesString)-1;
                                            MC.AvailableDivicesAmount.put(modelNamesString , prevAmount);

                                        }
                                        else{

                                            System.out.println("Loading...");

                                        }

                                    }
                                    else{

                                        MC.ListOfSales(codeSale, ModelName, phoneNumberCustomer, nameCustomer, SaleDate, finalCost);
                                        int prevAmount = MC.AvailableDivicesAmount.get(modelNamesString)-1;
                                        MC.AvailableDivicesAmount.put(modelNamesString , prevAmount);

                                    }      
    
                                }
                                else{

                                    System.out.println("Loading...");

                                }
    
                                }
                                else{
        
                                    System.out.println("Not available");
                                    System.out.println("0)Order\n1)HomePage");
                                    int choice = sc.nextInt();
                                    if(choice == 0){

                                        System.out.println("Type the order code: ");
                                        String codeOrder = sc.next();
                                        
                                        System.out.println("Customer's name: ");
                                        String nameCustomer = sc.next();
        
                                        System.out.println("Customer's phone number: ");
                                        String phoneNumberCustomer = sc.next();
        
                                        System.out.println("Date of order(dd-mm-yyyy): ");
                                        String dateOrder  = sc.next();
                                        
                                        System.out.println("Redemption date receipts(dd-mm-yyyy): ");
                                        String dateExpectOrder  = sc.next();
                                        
      
                                        String orderStatus  = "Processing";
                                        Date OrderDate = new SimpleDateFormat("dd-MM-yyyy").parse(dateOrder);
                                        Date OrderDateExpected = new SimpleDateFormat("dd-MM-yyyy").parse(dateExpectOrder);

                                        System.out.println("Use coupon Discount\n1)yah\n2)nah: ");

                                        int AvailableCoupon  = sc.nextInt();
                                        int trying = 0;

                                        Double finalCost = ModelName.getPriceModel();

                                        while((AvailableCoupon != 2)&&(trying <3)){
                                        
                                            System.out.println("Type the code of discount coupon("+(3-trying)+" tries): ");
                                            String codeCoupon  = sc.next();
                                            if(MC.CouponDiscount.containsKey(codeCoupon)){
    
                                                finalCost = ModelName.getPriceModel();
                                                int value = MC.CouponDiscount.get(codeCoupon);
                                                Double doublevalue = (double) value;
                                                finalCost = finalCost - (doublevalue/100.0)*finalCost;
    
                                            }
                                            else{
    
                                                System.out.println("Wrong code");
                 
                                            }
                                            trying++;
    
                                        }
                                        if(finalCost == ModelName.getPriceModel()){
    
                                            System.out.println("Do you want to continue the order?\n1)yes\n2)no");
                                            int finalDec = sc.nextInt();
                                            if( finalDec == 1 ){   

                                                MC.ListOfOrders(codeOrder, ModelName, phoneNumberCustomer, nameCustomer, OrderDate, OrderDateExpected, finalCost, orderStatus);
                                            
                                            }
                                            else{
                                                System.out.println("Loading...");
                                            }
                                        }
                                        else{

                                            MC.ListOfOrders(codeOrder, ModelName, phoneNumberCustomer, nameCustomer, OrderDate, OrderDateExpected, finalCost, orderStatus);
                                       
                                        }
 
                                    }
                                    else{

                                        System.out.println("Loading...");
    
                                    }
 
                                }

                        }
                        else{

                            System.out.println("Loading...");

                        }

                    }
                    else if(devicesMenuValue == 2){
                        System.out.println("1)Flaystation-5\n2)XcanisterS\n3)HomePage");
                        int  choise = sc.nextInt();
                        Console ModelName;
                        String modelNamesString;


                        if(choise == 1){

                            modelNamesString = "Flaystation-5";
                            ModelName = consoleDevice1;

                        }
                        else if(choise == 2){

                            ModelName = consoleDevice2;
                            modelNamesString = "XcanisterS";


                        }
                        else{

                            ModelName = null;
                            modelNamesString = "";
                        }
                        if(ModelName != null){

                            System.out.println(ModelName);
                            System.out.println(ModelName.printInfo());


                            if(MC.AvailableDivicesAmount.get(modelNamesString) != 0){

                                System.out.println("1)Buy\n2)HomePage");
                                int choice = sc.nextInt();
                                if(choice == 1){
                                    System.out.println("Type the sale code: ");
                                    String codeSale = sc.next();
                                    
                                    System.out.println("Customer's name: ");
                                    String nameCustomer = sc.next();
    
                                    System.out.println("Customer's phone number: ");
                                    String phoneNumberCustomer = sc.next();
    
                                    System.out.println("Date of sale(dd-mm-yyyy): ");
                                    String dateSale  = sc.next();

                                    Double finalCost = ModelName.getPriceModel();
                                     
                                    Date SaleDate = new SimpleDateFormat("dd-MM-yyyy").parse(dateSale);

                                    System.out.println("Use coupon Discount\n1)yah\n2)nah: ");
                                    
                                        //!!  coupondiscount  !!

                                    int AvailableCoupon  = sc.nextInt();
                                    int trying = 0;
                                     
                                    while((AvailableCoupon != 2)&&(trying <3)){
                                        
                                        System.out.println("Type the code of discount coupon("+(3-trying)+" tries): ");
                                        String codeCoupon  = sc.next();
                                        if(MC.CouponDiscount.containsKey(codeCoupon)){

                                            finalCost = ModelName.getPriceModel();
                                            int value = MC.CouponDiscount.get(codeCoupon);
                                            Double doublevalue = (double) value;
                                            finalCost = finalCost - (doublevalue/100.0)*finalCost;

                                        }
                                        else{

                                            System.out.println("Wrong code");
                                            
                                        }
                                        trying++;

                                    }
                                    if(finalCost == MC.AvailableDivices.get(modelNamesString).getPriceModel()){

                                        System.out.println("Do you want to continue the order?\n1)yes\n2)no");
                                        int finalDec = sc.nextInt();
                                        if( finalDec == 1 ){

                                            MC.ListOfSales(codeSale, ModelName, phoneNumberCustomer, nameCustomer, SaleDate, finalCost);
                                            int prevAmount = MC.AvailableDivicesAmount.get(modelNamesString)-1;
                                            MC.AvailableDivicesAmount.put(modelNamesString , prevAmount);

                                        }
                                        else{

                                            System.out.println("Loading...");

                                        }

                                    }
                                    else{

                                        MC.ListOfSales(codeSale, ModelName, phoneNumberCustomer, nameCustomer, SaleDate, finalCost);
                                        int prevAmount = MC.AvailableDivicesAmount.get(modelNamesString)-1;
                                        MC.AvailableDivicesAmount.put(modelNamesString , prevAmount);

                                    }      
    
                                }
                                else{

                                    System.out.println("Loading...");

                                }
    
                                }
                                else{
        
                                    System.out.println("Not available");
                                    System.out.println("0)Order\n1)HomePage");
                                    int choice = sc.nextInt();
                                    if(choice == 0){

                                        System.out.println("Type the order code: ");
                                        String codeOrder = sc.next();
                                        
                                        System.out.println("Customer's name: ");
                                        String nameCustomer = sc.next();
        
                                        System.out.println("Customer's phone number: ");
                                        String phoneNumberCustomer = sc.next();
        
                                        System.out.println("Date of order(dd-mm-yyyy): ");
                                        String dateOrder  = sc.next();
                                        
                                        System.out.println("Redemption date receipts(dd-mm-yyyy): ");
                                        String dateExpectOrder  = sc.next();
                                        
      
                                        String orderStatus  = "Processing";
                                        Date OrderDate = new SimpleDateFormat("dd-MM-yyyy").parse(dateOrder);
                                        Date OrderDateExpected = new SimpleDateFormat("dd-MM-yyyy").parse(dateExpectOrder);

                                        System.out.println("Use coupon Discount\n1)yah\n2)nah: ");

                                        int AvailableCoupon  = sc.nextInt();
                                        int trying = 0;

                                        Double finalCost = ModelName.getPriceModel();

                                        while((AvailableCoupon != 2)&&(trying <3)){
                                        
                                            System.out.println("Type the code of discount coupon("+(3-trying)+" tries): ");
                                            String codeCoupon  = sc.next();
                                            if(MC.CouponDiscount.containsKey(codeCoupon)){
    
                                                finalCost = ModelName.getPriceModel();
                                                int value = MC.CouponDiscount.get(codeCoupon);
                                                Double doublevalue = (double) value;
                                                finalCost = finalCost - (doublevalue/100.0)*finalCost;
    
                                            }
                                            else{
    
                                                System.out.println("Wrong code");
                 
                                            }
                                            trying++;
    
                                        }
                                        if(finalCost == ModelName.getPriceModel()){
    
                                            System.out.println("Do you want to continue the order?\n1)yes\n2)no");
                                            int finalDec = sc.nextInt();
                                            if( finalDec == 1 ){   

                                                MC.ListOfOrders(codeOrder, ModelName, phoneNumberCustomer, nameCustomer, OrderDate, OrderDateExpected, finalCost, orderStatus);
                                            
                                            }
                                            else{
                                                System.out.println("Loading...");
                                            }
                                        }
                                        else{

                                            MC.ListOfOrders(codeOrder, ModelName, phoneNumberCustomer, nameCustomer, OrderDate, OrderDateExpected, finalCost, orderStatus);
                                       
                                        }
 
                                    }
                                    else{

                                        System.out.println("Loading...");
    
                                    }
 
                                }

                        }
                        else{

                            System.out.println("Loading...");

                        }

                    }
                    else{

                        System.out.println("Loading...");

                    }

                }
                else if(miniMenuValue == 1){
                    System.out.println("0)Refrigerator\n1)WashingMachine\n2)HomePage");
                    devicesMenuValue = sc.nextInt();
                    if(devicesMenuValue == 0){

                        System.out.println("1)MAXSUNG-Smart-Iverter-Compressor-WIFI\n2)LJ-RS4000-Fridge-Freezer\n3)HomePage");
                        int  choise = sc.nextInt();
                        Refrigerator ModelName;
                        String modelNamesString;


                        if(choise == 1){

                            modelNamesString = "MAXSUNG-Smart-Iverter-Compressor-WIFI";
                            ModelName = refrigeratorD1;

                        }
                        else if(choise == 2){

                            ModelName = refrigeratorD2;
                            modelNamesString = "LJ-RS4000-Fridge-Freezer";


                        }
                        else{

                            ModelName = null;
                            modelNamesString = "";
                        }
                        if(ModelName != null){

                            System.out.println(ModelName);
                            System.out.println(ModelName.printInfo());


                            if(MC.AvailableDivicesAmount.get(modelNamesString) != 0){

                                System.out.println("1)Buy\n2)HomePage");
                                int choice = sc.nextInt();
                                if(choice == 1){
                                    System.out.println("Type the sale code: ");
                                    String codeSale = sc.next();
                                    
                                    System.out.println("Customer's name: ");
                                    String nameCustomer = sc.next();
    
                                    System.out.println("Customer's phone number: ");
                                    String phoneNumberCustomer = sc.next();
    
                                    System.out.println("Date of sale(dd-mm-yyyy): ");
                                    String dateSale  = sc.next();

                                    Double finalCost = ModelName.getPriceModel();
                                     
                                    Date SaleDate = new SimpleDateFormat("dd-MM-yyyy").parse(dateSale);

                                    System.out.println("Use coupon Discount\n1)yah\n2)nah: ");
                                    
                                        //!!  coupondiscount  !!

                                    int AvailableCoupon  = sc.nextInt();
                                    int trying = 0;
                                     
                                    while((AvailableCoupon != 1)&&(trying <3)){
                                        
                                        System.out.println("Type the code of discount coupon("+(3-trying)+" tries): ");
                                        String codeCoupon  = sc.next();
                                        if(MC.CouponDiscount.containsKey(codeCoupon)){

                                            finalCost = ModelName.getPriceModel();
                                            int value = MC.CouponDiscount.get(codeCoupon);
                                            Double doublevalue = (double) value;
                                            finalCost = finalCost - (doublevalue/100.0)*finalCost;

                                        }
                                        else{

                                            System.out.println("Wrong code");
                                            
                                        }
                                        trying++;

                                    }
                                    if(finalCost == MC.AvailableDivices.get(modelNamesString).getPriceModel()){

                                        System.out.println("Do you want to continue the order?\n1)yes\n2)no");
                                        int finalDec = sc.nextInt();
                                        if( finalDec == 1 ){

                                            MC.ListOfSales(codeSale, ModelName, phoneNumberCustomer, nameCustomer, SaleDate, finalCost);
                                            int prevAmount = MC.AvailableDivicesAmount.get(modelNamesString)-1;
                                            MC.AvailableDivicesAmount.put(modelNamesString , prevAmount);

                                        }
                                        else{

                                            System.out.println("Loading...");

                                        }

                                    }
                                    else{

                                        MC.ListOfSales(codeSale, ModelName, phoneNumberCustomer, nameCustomer, SaleDate, finalCost);
                                        int prevAmount = MC.AvailableDivicesAmount.get(modelNamesString)-1;
                                        MC.AvailableDivicesAmount.put(modelNamesString , prevAmount);

                                    }      
    
                                }
                                else{

                                    System.out.println("Loading...");

                                }
    
                                }
                                else{
        
                                    System.out.println("Not available");
                                    System.out.println("0)Order\n1)HomePage");
                                    int choice = sc.nextInt();
                                    if(choice == 0){

                                        System.out.println("Type the order code: ");
                                        String codeOrder = sc.next();
                                        
                                        System.out.println("Customer's name: ");
                                        String nameCustomer = sc.next();
        
                                        System.out.println("Customer's phone number: ");
                                        String phoneNumberCustomer = sc.next();
        
                                        System.out.println("Date of order(dd-mm-yyyy): ");
                                        String dateOrder  = sc.next();
                                        
                                        System.out.println("Redemption date receipts(dd-mm-yyyy): ");
                                        String dateExpectOrder  = sc.next();
                                        
      
                                        String orderStatus  = "Processing";
                                        Date OrderDate = new SimpleDateFormat("dd-MM-yyyy").parse(dateOrder);
                                        Date OrderDateExpected = new SimpleDateFormat("dd-MM-yyyy").parse(dateExpectOrder);

                                        System.out.println("Use coupon Discount\n1)yah\n2)nah: ");

                                        int AvailableCoupon  = sc.nextInt();
                                        int trying = 0;

                                        Double finalCost = ModelName.getPriceModel();

                                        while((AvailableCoupon != 2)&&(trying <3)){
                                        
                                            System.out.println("Type the code of discount coupon("+(3-trying)+" tries): ");
                                            String codeCoupon  = sc.next();
                                            if(MC.CouponDiscount.containsKey(codeCoupon)){
    
                                                finalCost = ModelName.getPriceModel();
                                                int value = MC.CouponDiscount.get(codeCoupon);
                                                Double doublevalue = (double) value;
                                                finalCost = finalCost - (doublevalue/100.0)*finalCost;
    
                                            }
                                            else{
    
                                                System.out.println("Wrong code");
                 
                                            }
                                            trying++;
    
                                        }
                                        if(finalCost == ModelName.getPriceModel()){
    
                                            System.out.println("Do you want to continue the order?\n1)yes\n2)no");
                                            int finalDec = sc.nextInt();
                                            if( finalDec == 1 ){   

                                                MC.ListOfOrders(codeOrder, ModelName, phoneNumberCustomer, nameCustomer, OrderDate, OrderDateExpected, finalCost, orderStatus);
                                            
                                            }
                                            else{
                                                System.out.println("Loading...");
                                            }
                                        }
                                        else{

                                            MC.ListOfOrders(codeOrder, ModelName, phoneNumberCustomer, nameCustomer, OrderDate, OrderDateExpected, finalCost, orderStatus);
                                       
                                        }
 
                                    }
                                    else{

                                        System.out.println("Loading...");
    
                                    }
 
                                }

                        }
                        else{

                            System.out.println("Loading...");

                        }

                    }
                    else if(devicesMenuValue == 1){

                        System.out.println("1)LJ-9kg-1400-revolution-WIFI-White-Inox\n2)Maxsung-6kg-100-revolution-WIFI-Black-Inox\n3)HomePage");
                        int  choise = sc.nextInt();
                        WashingMachine ModelName;
                        String modelNamesString;


                        if(choise == 1){

                            modelNamesString = "LJ-9kg-1400-revolution-WIFI-White-Inox";
                            ModelName = washingMach1;

                        }
                        else if(choise == 2){

                            ModelName = washingMach2;
                            modelNamesString = "Maxsung-6kg-100-revolution-WIFI-Black-Inox";


                        }
                        else{

                            ModelName = null;
                            modelNamesString = "";
                        }
                        if(ModelName != null){

                            System.out.println(ModelName);
                            System.out.println(ModelName.printInfo());


                            if(MC.AvailableDivicesAmount.get(modelNamesString) != 0){

                                System.out.println("1)Buy\n2)HomePage");
                                int choice = sc.nextInt();
                                if(choice == 1){
                                    System.out.println("Type the sale code: ");
                                    String codeSale = sc.next();
                                    
                                    System.out.println("Customer's name: ");
                                    String nameCustomer = sc.next();
    
                                    System.out.println("Customer's phone number: ");
                                    String phoneNumberCustomer = sc.next();
    
                                    System.out.println("Date of sale(dd-mm-yyyy): ");
                                    String dateSale  = sc.next();

                                    Double finalCost = ModelName.getPriceModel();
                                     
                                    Date SaleDate = new SimpleDateFormat("dd-MM-yyyy").parse(dateSale);

                                    System.out.println("Use coupon Discount\n1)yah\n2)nah: ");
                                    
                                        //!!  coupondiscount  !!

                                    int AvailableCoupon  = sc.nextInt();
                                    int trying = 0;
                                     
                                    while((AvailableCoupon != 1)&&(trying <3)){
                                        
                                        System.out.println("Type the code of discount coupon("+(3-trying)+" tries): ");
                                        String codeCoupon  = sc.next();
                                        if(MC.CouponDiscount.containsKey(codeCoupon)){

                                            finalCost = ModelName.getPriceModel();
                                            int value = MC.CouponDiscount.get(codeCoupon);
                                            Double doublevalue = (double) value;
                                            finalCost = finalCost - (doublevalue/100.0)*finalCost;

                                        }
                                        else{

                                            System.out.println("Wrong code");
                                            
                                        }
                                        trying++;

                                    }
                                    if(finalCost == MC.AvailableDivices.get(modelNamesString).getPriceModel()){

                                        System.out.println("Do you want to continue the order?\n1)yes\n2)no");
                                        int finalDec = sc.nextInt();
                                        if( finalDec == 1 ){

                                            MC.ListOfSales(codeSale, ModelName, phoneNumberCustomer, nameCustomer, SaleDate, finalCost);
                                            int prevAmount = MC.AvailableDivicesAmount.get(modelNamesString)-1;
                                            MC.AvailableDivicesAmount.put(modelNamesString , prevAmount);

                                        }
                                        else{

                                            System.out.println("Loading...");

                                        }

                                    }
                                    else{

                                        MC.ListOfSales(codeSale, ModelName, phoneNumberCustomer, nameCustomer, SaleDate, finalCost);
                                        int prevAmount = MC.AvailableDivicesAmount.get(modelNamesString)-1;
                                        MC.AvailableDivicesAmount.put(modelNamesString , prevAmount);

                                    }      
    
                                }
                                else{

                                    System.out.println("Loading...");

                                }
    
                                }
                                else{
        
                                    System.out.println("Not available");
                                    System.out.println("0)Order\n1)HomePage");
                                    int choice = sc.nextInt();
                                    if(choice == 0){

                                        System.out.println("Type the order code: ");
                                        String codeOrder = sc.next();
                                        
                                        System.out.println("Customer's name: ");
                                        String nameCustomer = sc.next();
        
                                        System.out.println("Customer's phone number: ");
                                        String phoneNumberCustomer = sc.next();
        
                                        System.out.println("Date of order(dd-mm-yyyy): ");
                                        String dateOrder  = sc.next();
                                        
                                        System.out.println("Redemption date receipts(dd-mm-yyyy): ");
                                        String dateExpectOrder  = sc.next();
                                        
      
                                        String orderStatus  = "Processing";
                                        Date OrderDate = new SimpleDateFormat("dd-MM-yyyy").parse(dateOrder);
                                        Date OrderDateExpected = new SimpleDateFormat("dd-MM-yyyy").parse(dateExpectOrder);

                                        System.out.println("Use coupon Discount\n1)yah\n2)nah: ");

                                        int AvailableCoupon  = sc.nextInt();
                                        int trying = 0;

                                        Double finalCost = ModelName.getPriceModel();

                                        while((AvailableCoupon != 2)&&(trying <3)){
                                        
                                            System.out.println("Type the code of discount coupon("+(3-trying)+" tries): ");
                                            String codeCoupon  = sc.next();
                                            if(MC.CouponDiscount.containsKey(codeCoupon)){
    
                                                finalCost = ModelName.getPriceModel();
                                                int value = MC.CouponDiscount.get(codeCoupon);
                                                Double doublevalue = (double) value;
                                                finalCost = finalCost - (doublevalue/100.0)*finalCost;
    
                                            }
                                            else{
    
                                                System.out.println("Wrong code");
                 
                                            }
                                            trying++;
    
                                        }
                                        if(finalCost == ModelName.getPriceModel()){
    
                                            System.out.println("Do you want to continue the order?\n1)yes\n2)no");
                                            int finalDec = sc.nextInt();
                                            if( finalDec == 1 ){   

                                                MC.ListOfOrders(codeOrder, ModelName, phoneNumberCustomer, nameCustomer, OrderDate, OrderDateExpected, finalCost, orderStatus);
                                            
                                            }
                                            else{
                                                System.out.println("Loading...");
                                            }
                                        }
                                        else{

                                            MC.ListOfOrders(codeOrder, ModelName, phoneNumberCustomer, nameCustomer, OrderDate, OrderDateExpected, finalCost, orderStatus);
                                       
                                        }
 
                                    }
                                    else{

                                        System.out.println("Loading...");
    
                                    }
 
                                }

                        }
                        else{

                            System.out.println("Loading...");

                        }
                    }
                    else{
                        System.out.println("Loading...");
                        
                    }

                    
                }
            }
            else if(menuValue == 1){
                
                System.out.println("Type the order code: ");
                String OrderCode = sc.next();
                
                if(MC.OrdersListPos.containsKey(OrderCode)){

                    System.out.println(MC.OrdersList.get(MC.returnPos(OrderCode)));
                    System.out.println("0) Order arrival\n1)HomePage ");
                    int choice = sc.nextInt();
                    if(choice == 0){

                        MC.OrdersList.get(MC.returnPos(OrderCode)).SetNewStatus("Arrival");

                        MC.OrderToSale(MC.OrdersList.get(MC.returnPos(OrderCode)));


                    }
                    else{

                    System.out.println("Loading...");


                    }

                }
                else{

                    System.out.println("This code cannot be matched with other order codes");


                }



            }
            else{

                System.out.println(MC.salesList);

            }

            System.out.println("0) Device overview\n1) Orders overview\n2) Sales overview\n3) Exit");
            menuValue = sc.nextInt();

        }
        sc.close();
    }
}