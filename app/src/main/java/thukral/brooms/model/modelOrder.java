package thukral.brooms.model;

public class modelOrder {
    private String order_id ;
    private String txn_id ;
    private String order_code ;
    private String product_id ;
    private String quantity ;
    private String user_id ;



    private String distributor_name ;
    private String distributor_phone ;
    private String distributor_address ;
    private String distributor_city ;
    private String distributor_zone ;
    private String distributor_state ;

    private String name ;
    private String category_name ;
    private String sub_name ;

    private String sales_price ;
    private String Image ;
    private String total_price ;

    private String deliver_days ;
    private String payment_status ;
    private String payment_mode ;
    private String status ;
    private String invoice ;
    private String date_created ;
    private String total_piece ;

    public modelOrder(String order_id, String txn_id, String order_code, String product_id, String quantity, String user_id, String distributor_name, String distributor_phone, String distributor_address, String distributor_city, String distributor_zone, String distributor_state, String name, String category_name, String sub_name, String sales_price, String image, String total_price, String deliver_days, String payment_status, String payment_mode, String status, String invoice, String date_created, String total_piece) {
        this.order_id = order_id;
        this.txn_id = txn_id;
        this.order_code = order_code;
        this.product_id = product_id;
        this.quantity = quantity;
        this.user_id = user_id;
        this.distributor_name = distributor_name;
        this.distributor_phone = distributor_phone;
        this.distributor_address = distributor_address;
        this.distributor_city = distributor_city;
        this.distributor_zone = distributor_zone;
        this.distributor_state = distributor_state;
        this.name = name;
        this.category_name = category_name;
        this.sub_name = sub_name;
        this.sales_price = sales_price;
        Image = image;
        this.total_price = total_price;
        this.deliver_days = deliver_days;
        this.payment_status = payment_status;
        this.payment_mode = payment_mode;
        this.status = status;
        this.invoice = invoice;
        this.date_created = date_created;
        this.total_piece = total_piece;
    }

    public String getOrder_id() {
        return order_id;
    }

    public String getTxn_id() {
        return txn_id;
    }

    public String getOrder_code() {
        return order_code;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getDistributor_name() {
        return distributor_name;
    }

    public String getDistributor_phone() {
        return distributor_phone;
    }

    public String getDistributor_address() {
        return distributor_address;
    }

    public String getDistributor_city() {
        return distributor_city;
    }

    public String getDistributor_zone() {
        return distributor_zone;
    }

    public String getDistributor_state() {
        return distributor_state;
    }

    public String getName() {
        return name;
    }

    public String getCategory_name() {
        return category_name;
    }

    public String getSub_name() {
        return sub_name;
    }

    public String getSales_price() {
        return sales_price;
    }

    public String getImage() {
        return Image;
    }

    public String getTotal_price() {
        return total_price;
    }

    public String getDeliver_days() {
        return deliver_days;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public String getPayment_mode() {
        return payment_mode;
    }

    public String getStatus() {
        return status;
    }

    public String getInvoice() {
        return invoice;
    }

    public String getDate_created() {
        return date_created;
    }

    public String getTotal_piece() {
        return total_piece;
    }
}
