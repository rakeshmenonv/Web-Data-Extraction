package $!{dao_package};

import $!{entity_classpath};

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface $!{className}Dao extends PagingAndSortingRepository<$!{className}, Long>, JpaSpecificationExecutor<$!{className}>  {

}