package thukral.brooms.model;

public class modelCartList {



    private String product_id ;
    private String user_id ;
    private String name ;
    private String cat_id ;
    private String sub_id ;
    private String category_name ;
    private String sub_name ;
    private String description ;
    private String unit ;
    private String set_qty ;
    private String quantity ;
    private String total_piece ;
    private String price ;
    private String sales_price ;
    private String total_set_price ;
    private String total_price ;
    private String Image ;
    private String date_created ;
    private String cart_created ;


    public modelCartList(String product_id, String user_id, String name, String cat_id, String sub_id, String category_name, String sub_name, String description, String unit, String set_qty, String quantity, String total_piece, String price, String sales_price, String total_set_price, String total_price, String image, String date_created, String cart_created) {
        this.product_id = product_id;
        this.user_id = user_id;
        this.name = name;
        this.cat_id = cat_id;
        this.sub_id = sub_id;
        this.category_name = category_name;
        this.sub_name = sub_name;
        this.description = description;
        this.unit = unit;
        this.set_qty = set_qty;
        this.quantity = quantity;
        this.total_piece = total_piece;
        this.price = price;
        this.sales_price = sales_price;
        this.total_set_price = total_set_price;
        this.total_price = total_price;
        Image = image;
        this.date_created = date_created;
        this.cart_created = cart_created;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getName() {
        return name;
    }

    public String getCat_id() {
        return cat_id;
    }

    public String getSub_id() {
        return sub_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public String getSub_name() {
        return sub_name;
    }

    public String getDescription() {
        return description;
    }

    public String getUnit() {
        return unit;
    }

    public String getSet_qty() {
        return set_qty;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getTotal_piece() {
        return total_piece;
    }

    public String getPrice() {
        return price;
    }

    public String getSales_price() {
        return sales_price;
    }

    public String getTotal_set_price() {
        return total_set_price;
    }

    public String getTotal_price() {
        return total_price;
    }

    public String getImage() {
        return Image;
    }

    public String getDate_created() {
        return date_created;
    }

    public String getCart_created() {
        return cart_created;
    }
}
