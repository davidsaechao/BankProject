import java.io.*;
import java.util.*;
import java.awt.*;

		//Transactions object
		public class Transactions implements Serializable{
			private String date;
			private String type;
			private int checkNo;
			private String transDescription;
			private float payDeb;
			private float depCred;
			private float updatedBal;
			public Transactions(String date, String type, int checkNo, String transDescription, float payDeb,
																	 float depCred, float updatedBal) {
				this.date = date;
				this.type = type;
				this.checkNo = checkNo;
				this.transDescription = transDescription;
				this.payDeb = payDeb;
				this.depCred = depCred;
				this.updatedBal = updatedBal;
			}
			public String getDate() {
				return date;
			}
			public String getType() {
				return type;
			}
			public int getCheckNo() {
				return checkNo;
			}
			public String getTransDescription() {
				return transDescription;
			}
			public float getPayDeb() {
				return payDeb;
			}
			public float getDepCred() {
				return depCred;
			}
			public float getUpdatedBal() {
				return updatedBal;
			}
			public void printAll() {
				System.out.printf("%3s %3s %3d %3s %3f %3f %3f\n",date,type,checkNo,transDescription,payDeb,depCred,updatedBal);
			}
		}