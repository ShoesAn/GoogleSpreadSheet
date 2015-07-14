package terry.example.spread;

import com.google.gdata.data.docs.Track;

import android.os.Parcel;
import android.os.Parcelable;

public class Columns implements Parcelable{
	
	private int id;
	private double number;
	private long DATE;
	private String remark;
	private String spreadsheet_url;
	private String state;
	private String type;
	
	public Columns(){}
	
	public Columns(double number, long DATE, String remark, String spreadsheet_url, String state, String type) {
		  super();
		  this.setNumber(number);
		  this.setDATE(DATE);
		  this.setRemark(remark);
		  this.setUrl(spreadsheet_url);
		  this.setState(state);
		  this.setType(type);
	}
	
	

	public void setState(String string) {
		// TODO 
		this.state = string;
	}
	
	public void setRemark(String remark) {
		// TODO 
		this.remark = remark;
	}
	public void setDATE(long DATE) {
		// TODO 
		this.DATE = DATE;
	}
	public void setNumber(double number) {
		// TODO 
		this.number = number;
	}
	public void setId(int id) {
		// TODO 
		this.id = id;
	}
	
	public void setUrl(String spreadsheet_url){
		this.spreadsheet_url = spreadsheet_url;
	}
	
	public void setType(String type){
		this.type = type;
	}
	
	public String toString() {
	    return "Columns [id=" + id + ", number=" + getNumber() 
	     + ", DATE=" + getDATE() + ", remark=" + getRemark() + 
	     ", state=" + getState() + ", type=" + getType() +
	     "]"
	     ;
	}
	
	public double getNumber() {
		return number;
	}
	
	public long getDATE() {
		return DATE;
	}
	
	public String getRemark() {
		return remark;
	}
	
	public String getState() {
		return state;
	}
	
	
	public String getUrl() {
		return spreadsheet_url;
	}
	
	public String getType() {
		return type;
	}
	
	
	public int getId() {
		// TODO 
		return this.id;
	}

	@Override
	public int describeContents() {
		// TODO 自動產生的方法 Stub
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO 自動產生的方法 Stub
		dest.writeString(this.remark);
		dest.writeString(this.spreadsheet_url);
		dest.writeString(this.state);
		dest.writeString(this.type);
		dest.writeDouble(this.number);
		dest.writeInt(this.id);
		dest.writeLong(this.DATE);
		
	}
	
	@SuppressWarnings("rawtypes")
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
	    @Override
	    public Columns createFromParcel(Parcel in) {
	        return new Columns(in);
	    }

	    @Override
	    public Columns[] newArray(int size) {
	        return new Columns[size];
	    }
	};
	public Columns(Parcel in) {
		// TODO 自動產生的建構子 Stub
		remark = in.readString();
		spreadsheet_url = in.readString();
		state = in.readString();
		type = in.readString();
		number = in.readDouble();
		id = in.readInt();
		DATE = in.readLong();
	}
}
