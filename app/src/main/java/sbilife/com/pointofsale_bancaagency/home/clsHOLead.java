package sbilife.com.pointofsale_bancaagency.home;

/*
 * while adding event in calendar it is hold by this class.
 */

public class clsHOLead {

    private String _date;
    private String _custid;
    private String _custname;
    private String _priority;
    private String _status;
    private String _substatus;
    private String _proposalno;
    private String _followupdate;
    private String _comments;
    private String _age;
    private String _totalacc;
    private String _balance;
    private String _branchcode;
    private String _userid;
    private String _sync;
    private String _leadid;
    private String _name;
    private String _source;

    public clsHOLead(String _date, String _custid, String _custname,
                     String _priority, String _status, String _substatus,
                     String _proposalno, String _followupdate, String _comments,
                     String _age, String _totalacc, String _balance, String _branchcode,
                     String _userid, String _sync, String _leadid, String _name, String _source) {
        super();
        this._date = _date;
        this._custid = _custid;
        this._custname = _custname;
        this._priority = _priority;
        this._status = _status;
        this._substatus = _substatus;
        this._proposalno = _proposalno;
        this._followupdate = _followupdate;
        this._comments = _comments;
        this._age = _age;
        this._totalacc = _totalacc;
        this._balance = _balance;
        this._branchcode = _branchcode;
        this._userid = _userid;
        this._sync = _sync;
        this._leadid = _leadid;
        this._name = _name;
        this._source = _source;
    }

    public String get_date() {
        return _date;
    }

    public void set_date(String _date) {
        this._date = _date;
    }

    public String get_custid() {
        return _custid;
    }

    public void set_custid(String _custid) {
        this._custid = _custid;
    }

    public String get_custname() {
        return _custname;
    }

    public void set_custname(String _custname) {
        this._custname = _custname;
    }

    public String get_priority() {
        return _priority;
    }

    public void set_priority(String _priority) {
        this._priority = _priority;
    }

    public String get_status() {
        return _status;
    }

    public void set_status(String _status) {
        this._status = _status;
    }

    public String get_substatus() {
        return _substatus;
    }

    public void set_substatus(String _substatus) {
        this._substatus = _substatus;
    }

    public String get_proposalno() {
        return _proposalno;
    }

    public void set_proposalno(String _proposalno) {
        this._proposalno = _proposalno;
    }

    public String get_followupdate() {
        return _followupdate;
    }

    public void set_followupdate(String _followupdate) {
        this._followupdate = _followupdate;
    }

    public String get_comments() {
        return _comments;
    }

    public void set_comments(String _comments) {
        this._comments = _comments;
    }

    public String get_age() {
        return _age;
    }

    public void set_age(String _age) {
        this._age = _age;
    }

    public String get_totalacc() {
        return _totalacc;
    }

    public void set_totalacc(String _totalacc) {
        this._totalacc = _totalacc;
    }

    public String get_balance() {
        return _balance;
    }

    public void set_balance(String _balance) {
        this._balance = _balance;
    }

    public String get_branchcode() {
        return _branchcode;
    }

    public void set_branchcode(String _branchcode) {
        this._branchcode = _branchcode;
    }

    public String get_userid() {
        return _userid;
    }

    public void set_userid(String _userid) {
        this._userid = _userid;
    }

    public String get_sync() {
        return _sync;
    }

    public void set_sync(String _sync) {
        this._sync = _sync;
    }

    public String get_leadid() {
        return _leadid;
    }

    public void set_leadid(String _leadid) {
        this._leadid = _leadid;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_source() {
        return _source;
    }

    public void set_source(String _source) {
        this._source = _source;
    }

}
