package client.client;

import java.io.Serializable;

public class Client implements Serializable {

	private String firstName;
	private String lastName;
	private String login;
	private String password;

	public Client(Builder builder) {
		this.firstName = builder.firstName;
		this.lastName = builder.lastName;
		this.login = builder.login;
		this.password = builder.password;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getLogin() {
		return login;
	}

	public String getPassword() {
		return password;
	}

	@Override
	public boolean equals(Object aClient) {
		Client client = (Client) aClient;
		String login = client.getLogin();
		return this.login.equals(login);
	}

	@Override
	public String toString() {
		return firstName + " " + lastName;
	}

	public static class Builder {

		String firstName;
		String lastName;
		String login;
		String password;

		public Client build() {
			return new Client(this);
		}

		public Builder firstName(String firstName) {
			this.firstName = firstName;
			return this;
		}

		public Builder lastName(String lastName) {
			this.lastName = lastName;
			return this;
		}

		public Builder login(String login) {
			this.login = login;
			return this;
		}

		public Builder password(String password) {
			this.password = password;
			return this;
		}
	}
}
