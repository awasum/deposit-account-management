/*
 * Copyright 2017 The Mifos Initiative.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.mifos.deposit;

import io.mifos.deposit.api.v1.EventConstants;
import io.mifos.deposit.api.v1.definition.domain.ProductDefinition;
import io.mifos.deposit.api.v1.instance.domain.ProductInstance;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class TestProductInstance extends AbstractDepositAccountManagementTest {

  public TestProductInstance() {
    super();
  }

  @Test
  public void shouldCreateProductInstance() throws Exception {
    final String customerIdentifier = "08154711";
    final ProductDefinition productDefinition = Fixture.productDefinition();

    super.depositAccountManager.create(productDefinition);

    super.eventRecorder.wait(EventConstants.POST_PRODUCT_DEFINITION, productDefinition.getIdentifier());

    final ProductInstance productInstance = new ProductInstance();
    productInstance.setProductIdentifier(productDefinition.getIdentifier());
    productInstance.setCustomerIdentifier(customerIdentifier);

    super.depositAccountManager.create(productInstance);

    super.eventRecorder.wait(EventConstants.POST_PRODUCT_INSTANCE, customerIdentifier);

    final List<ProductInstance> productInstances = super.depositAccountManager.findProductInstances(productDefinition.getIdentifier());
    Assert.assertNotNull(productInstances);
    Assert.assertEquals(1, productInstances.size());
    final ProductInstance foundProductInstance = productInstances.get(0);

    Assert.assertEquals(productDefinition.getEquityLedgerIdentifier() + "." + customerIdentifier + ".00001",
        foundProductInstance.getAccountIdentifier());
  }

  @Test
  public void shouldActivateProductInstance() throws Exception {
    final String customerIdentifier = "08154712";
    final ProductDefinition productDefinition = Fixture.productDefinition();

    super.depositAccountManager.create(productDefinition);

    super.eventRecorder.wait(EventConstants.POST_PRODUCT_DEFINITION, productDefinition.getIdentifier());

    final ProductInstance productInstance = new ProductInstance();
    productInstance.setProductIdentifier(productDefinition.getIdentifier());
    productInstance.setCustomerIdentifier(customerIdentifier);

    super.depositAccountManager.create(productInstance);

    super.eventRecorder.wait(EventConstants.POST_PRODUCT_INSTANCE, customerIdentifier);

    final List<ProductInstance> productInstances = super.depositAccountManager.findProductInstances(productDefinition.getIdentifier());
    Assert.assertNotNull(productInstances);
    Assert.assertEquals(1, productInstances.size());
    final ProductInstance foundProductInstance = productInstances.get(0);

    super.depositAccountManager.postProductInstanceCommand(
        foundProductInstance.getAccountIdentifier(), EventConstants.ACTIVATE_PRODUCT_INSTANCE_COMMAND);

    Assert.assertTrue(
        super.eventRecorder.wait(EventConstants.ACTIVATE_PRODUCT_INSTANCE,
            foundProductInstance.getAccountIdentifier()));
  }

  @Test
  public void shouldCloseProductInstance() throws Exception {
    final String customerIdentifier = "08154713";
    final ProductDefinition productDefinition = Fixture.productDefinition();

    super.depositAccountManager.create(productDefinition);

    super.eventRecorder.wait(EventConstants.POST_PRODUCT_DEFINITION, productDefinition.getIdentifier());

    final ProductInstance productInstance = new ProductInstance();
    productInstance.setProductIdentifier(productDefinition.getIdentifier());
    productInstance.setCustomerIdentifier(customerIdentifier);

    super.depositAccountManager.create(productInstance);

    super.eventRecorder.wait(EventConstants.POST_PRODUCT_INSTANCE, customerIdentifier);

    final List<ProductInstance> productInstances = super.depositAccountManager.findProductInstances(productDefinition.getIdentifier());
    Assert.assertNotNull(productInstances);
    Assert.assertEquals(1, productInstances.size());
    final ProductInstance foundProductInstance = productInstances.get(0);

    super.depositAccountManager.postProductInstanceCommand(
        foundProductInstance.getAccountIdentifier(), EventConstants.CLOSE_PRODUCT_INSTANCE_COMMAND);

    Assert.assertTrue(
        super.eventRecorder.wait(EventConstants.CLOSE_PRODUCT_INSTANCE,
            foundProductInstance.getAccountIdentifier()));
  }
}
