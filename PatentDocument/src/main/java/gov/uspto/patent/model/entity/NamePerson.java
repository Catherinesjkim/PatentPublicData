package gov.uspto.patent.model.entity;

import javax.naming.directory.InvalidAttributesException;

import com.google.common.base.Strings;

import gov.uspto.text.StringUtil;

/*
* TODO: need more research... name and entity disambiguation.
* 	Does the middle name or initial sometimes appear in the first-name field?
* 
* 		Function to get a list of name variants:
* 			first word in first name field,  with lastname field.
* 			first name last name
* 			first name, last name joined no space.
* 			first initial last name.
* 
* 		Function to get normalized "best guess" name.
* 
* 		Function to score uniqueness of names, need to create a domain name dictionary which contains uniqueness score within the corpus.
* 			john  0.0875
* 			doe	  0.0093
*			likely hood of word being lastname or first name... likely hood of finding first name and last name together...
*/
public class NamePerson extends Name {

    private String prefix; // Title: Mr., Mrs., Dr.
	private final String firstName;
	private final String middleName;
	private final String lastName;

    public NamePerson(String firstName, String lastName) throws InvalidAttributesException{
    	this(firstName, null, lastName);
    }

    public NamePerson(String firstName, String middleName, String lastName) throws InvalidAttributesException{
    	super(buildFullName(firstName, middleName, lastName));

		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;

		validate();
    }

	public String getLastName() {
		return lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public boolean validate() throws InvalidAttributesException{
		String fullName = super.getName();

		if (Strings.isNullOrEmpty(lastName)){
			throw new InvalidAttributesException("Invalid PersonName, lastname can not be blank");
		}

		if (Strings.isNullOrEmpty(fullName) || fullName.length() < 2){
			throw new InvalidAttributesException("Invalid PersonName, fullName can not be blank or smaller than 2.");
		}

		return true;
	}

    public String getAbbreviatedName(){
        if (Strings.isNullOrEmpty(firstName)){
            return lastName + ", " + firstName.substring(0, 1) + '.';
        } else {
            return lastName;
        }
    }

	public String getMiddleName() {
		return middleName;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	private static String buildFullName(String firstName, String middleName, String lastName){
		StringBuilder sb = new StringBuilder();
		if (!Strings.isNullOrEmpty(lastName)){
			sb.append(lastName).append(", ");
		}

		if (!Strings.isNullOrEmpty(firstName)){
			sb.append( StringUtil.join(" ", firstName, middleName) );
		}

		return sb.toString().trim();
	}

	@Override
	public String toString() {
		return "PersonName[prefix=" + prefix + ", firstName=" + firstName + ", middleName=" + middleName
				+ ", lastName=" + lastName + ", fullName=" + super.getName() + ", suffix=" + super.getSuffix() + ", synonym=" + super.getSynonyms() + "]";
	}
}