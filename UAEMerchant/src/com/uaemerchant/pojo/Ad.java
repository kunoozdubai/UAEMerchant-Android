package com.uaemerchant.pojo;

public class Ad {

	String adId;
	String userId;
	String catId;
	String title;
	String price;
	String city;
	String address;
	String longitude;
	String latitude;
	String description;
	String photo1;
	String photo2;
	String photo3;
	String created;
	String status;
	String email;
	String phone;
	String name;

	public Ad(String adId, String userId, String catId, String title, String price, String city, String address, String longitude, String latitude,
			String description, String photo1, String photo2, String photo3, String created, String status, String email, String phone, String name) {
		super();
		this.adId = adId;
		this.userId = userId;
		this.catId = catId;
		this.title = title;
		this.price = price;
		this.city = city;
		this.address = address;
		this.longitude = longitude;
		this.latitude = latitude;
		this.description = description;
		this.photo1 = photo1;
		this.photo2 = photo2;
		this.photo3 = photo3;
		this.created = created;
		this.status = status;
		this.email = email;
		this.phone = phone;
		this.name = name;
	}

	public String getAdId() {
		return adId;
	}

	public void setAdId(String adId) {
		this.adId = adId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCatId() {
		return catId;
	}

	public void setCatId(String catId) {
		this.catId = catId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPhoto1() {
		return photo1;
	}

	public void setPhoto1(String photo1) {
		this.photo1 = photo1;
	}

	public String getPhoto2() {
		return photo2;
	}

	public void setPhoto2(String photo2) {
		this.photo2 = photo2;
	}

	public String getPhoto3() {
		return photo3;
	}

	public void setPhoto3(String photo3) {
		this.photo3 = photo3;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
