package thukral.brooms.subModel;

public class modelAllMops {

    private  String user_id ;
    private  String user_type ;
    private  String id ;
    private  String cat_id ;
    private  String category_name ;
    private  String name ;
    private  String description ;
    private  String color ;
    private  String unit ;
    private  String bag ;
    private  String qty ;
    private  String regular_price ;
    private  String sales_price ;
    private  String image ;
    private  String date_created ;
    private  String wishlist ;

    public modelAllMops(String user_id, String user_type, String id, String cat_id, String category_name, String name, String description, String color, String unit, String bag, String qty, String regular_price, String sales_price, String image, String date_created, String wishlist) {
        this.user_id = user_id;
        this.user_type = user_type;
        this.id = id;
        this.cat_id = cat_id;
        this.category_name = category_name;
        this.name = name;
        this.description = description;
        this.color = color;
        this.unit = unit;
        this.bag = bag;
        this.qty = qty;
        this.regular_price = regular_price;
        this.sales_price = sales_price;
        this.image = image;
        this.date_created = date_created;
        this.wishlist = wishlist;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getUser_type() {
        return user_type;
    }

    public String getId() {
        return id;
    }

    public String getCat_id() {
        return cat_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getColor() {
        return color;
    }

    public String getUnit() {
        return unit;
    }

    public String getBag() {
        return bag;
    }

    public String getQty() {
        return qty;
    }

    public String getRegular_price() {
        return regular_price;
    }

    public String getSales_price() {
        return sales_price;
    }

    public String getImage() {
        return image;
    }

    public String getDate_created() {
        return date_created;
    }

    public String getWishlist() {
        return wishlist;
    }
}
