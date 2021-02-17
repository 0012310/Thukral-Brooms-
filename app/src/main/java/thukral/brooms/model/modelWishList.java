package thukral.brooms.model;

public class modelWishList {
    private  String  id ;
    private  String  name ;
    private  String  cat_id ;
    private  String  sub_id ;
    private  String  category_name ;
    private  String  sub_name ;
    private  String  description ;
    private  String  color ;
    private  String  unit ;
    private  String  bag ;
    private  String  qty ;
    private  String  regular_price ;
    private  String  sales_price ;
    private  String  total_set_price ;
    private  String  Image ;
    private  String  date_created ;


    public modelWishList(String id, String name, String cat_id, String sub_id, String category_name, String sub_name, String description, String color, String unit, String bag, String qty, String regular_price, String sales_price, String total_set_price, String image, String date_created) {
        this.id = id;
        this.name = name;
        this.cat_id = cat_id;
        this.sub_id = sub_id;
        this.category_name = category_name;
        this.sub_name = sub_name;
        this.description = description;
        this.color = color;
        this.unit = unit;
        this.bag = bag;
        this.qty = qty;
        this.regular_price = regular_price;
        this.sales_price = sales_price;
        this.total_set_price = total_set_price;
        Image = image;
        this.date_created = date_created;
    }

    public String getId() {
        return id;
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

    public String getTotal_set_price() {
        return total_set_price;
    }

    public String getImage() {
        return Image;
    }

    public String getDate_created() {
        return date_created;
    }
}
