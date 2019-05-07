/**
   * @hibernate.id  generator-class="assigned"
   **/
public String getId() {
    if (id_ == null) {
        StringBuffer b = new StringBuffer();
        b.append(name_);
        if (language_ != null)
            b.append('_').append(language_);
        //if(country_ != null) b.append('_').append(country_); 
        //if(variant_ != null) b.append('_').append(variant_); 
        id_ = b.toString();
    }
    return id_;
}
