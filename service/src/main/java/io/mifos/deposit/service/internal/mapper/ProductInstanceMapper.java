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
package io.mifos.deposit.service.internal.mapper;

import io.mifos.core.lang.ServiceException;
import io.mifos.deposit.api.v1.instance.domain.ProductInstance;
import io.mifos.deposit.service.internal.repository.ProductDefinitionEntity;
import io.mifos.deposit.service.internal.repository.ProductDefinitionRepository;
import io.mifos.deposit.service.internal.repository.ProductInstanceEntity;

import java.util.Optional;

public class ProductInstanceMapper {

  private ProductInstanceMapper() {
    super();
  }

  public static ProductInstanceEntity map(final ProductInstance productInstance,
                                          final ProductDefinitionRepository productDefinitionRepository) {
    final Optional<ProductDefinitionEntity> optionalProductDefinitionEntity = productDefinitionRepository.findByIdentifier(productInstance.getProductIdentifier());
    if (optionalProductDefinitionEntity.isPresent()) {
      final ProductInstanceEntity productInstanceEntity = new ProductInstanceEntity();
      productInstanceEntity.setCustomerIdentifier(productInstance.getCustomerIdentifier());
      productInstanceEntity.setAccountIdentifier(productInstance.getAccountIdentifier());
      productInstanceEntity.setState(productInstance.getState());
      productInstanceEntity.setProductDefinition(optionalProductDefinitionEntity.get());

      return productInstanceEntity;
    } else {
      throw ServiceException.notFound("Unable to assign product {0} to customer {1}, product not found.",
          productInstance.getProductIdentifier(), productInstance.getCustomerIdentifier());
    }
  }

  public static ProductInstance map(final ProductInstanceEntity productInstanceEntity) {
    final ProductInstance productInstance = new ProductInstance();
    productInstance.setCustomerIdentifier(productInstanceEntity.getCustomerIdentifier());
    productInstance.setAccountIdentifier(productInstanceEntity.getAccountIdentifier());
    productInstance.setProductIdentifier(productInstanceEntity.getProductDefinition().getIdentifier());
    productInstance.setState(productInstanceEntity.getState());

    return productInstance;
  }
}
