package rtspproxy.rtsp;

/**
 * RtspHeaderCode.
 */
public enum RtspHeaderCode {
	CSeq("CSeq"), 
	UserAgent("User-Agent"), 
	Accept("Accept"), 
	MayNotify("May-Notify"), 
	Require("Require"), 
	UUData("UUData"), 
	Transport("Transport"), 
	SecureData("SecureData"), 
	Date("Date"), 
	Location("Location"), 
	Session("Session"), 
	UserNotifiacation("User-Notifiacation"), 
	Range("Range"), 
	Scale("Scale"), 
	Notice("Notice"),
	Public("Public"), 
	Server("Server"), 
	ContentLength("Content-Length"),
	Connection("Connection"),
	//迪麓视频服务器 begin 
	OnDemandSessionId("OnDemandSessionId"),
	Volume("Volume"),
	SessionGroup("SessionGroup"),
	StreamControlProto("StreamControlProto"),
	Policy("Policy"),
	StartPoint("StartPoint"),
	ContentType("Content-Type"),
	Reason("Reason"),
	XNotice("x-notice");
	//迪麓视频服务器 end 

	private final String value;

	private RtspHeaderCode(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}

}
