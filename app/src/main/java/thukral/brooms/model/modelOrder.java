package thukral.brooms.model;

public class modelOrder {
    private String order_id ;
    private String txn_id ;
    private String order_code ;
    private String product_id ;
    private String quantity ;
    private String user_id ;
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


    public modelOrder(String order_id, String txn_id, String order_code, String product_id, String quantity, String user_id, String name, String category_name, String sub_name, String sales_price, String image, String total_price, String deliver_days, String payment_status, String payment_mode, String status, String invoice, String date_created, String total_piece) {
        this.order_id = order_id;
        this.txn_id = txn_id;
        this.order_code = order_code;
        this.product_id = product_id;
        this.quantity = quantity;
        this.user_id = user_id;
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
