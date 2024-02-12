package org.springframework.demo.container.xml.service;

import org.springframework.demo.container.xml.dao.AccountDao;
import org.springframework.demo.container.xml.dao.ItemDao;

/**
 * service层对象.
 *
 * @author dsy
 */
public class PetStoreServiceImpl {

	private AccountDao accountDao;

	private ItemDao itemDao;

	public AccountDao accountDao() {
		return accountDao;
	}

	public void setAccountDao(AccountDao accountDao) {
		this.accountDao = accountDao;
	}

	public ItemDao itemDao() {
		return itemDao;
	}

	public void setItemDao(ItemDao itemDao) {
		this.itemDao = itemDao;
	}
}
