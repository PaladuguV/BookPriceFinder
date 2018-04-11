package com.ourapp.booker;

//Class to hold the results data for each book, given by Google

public class ResultsData{
	String  title;
	String  author;
	String  publisher;
	String  date;
	String  description;
	String  isbn;
	String  pages;
	String  thumbnail;
	
	public ResultsData(String title,String author,String publisher,String date,String description,String isbn,String pages,String thumbnail){

	this.title = title;
	this.author = author;
	this.publisher = publisher;
	this.date = date;
	this.description = description;
	this.isbn = isbn;
	this.pages = pages;
	this.thumbnail = thumbnail;

	
	}

	}
