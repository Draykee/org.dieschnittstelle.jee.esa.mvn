package org.dieschnittstelle.jee.esa.ejb.client.junit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ TestStockSystem.class, TestProductCRUD.class })
public class TestStockAndProduct {
}
