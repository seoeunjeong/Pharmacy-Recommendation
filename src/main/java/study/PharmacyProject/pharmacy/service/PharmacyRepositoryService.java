package study.PharmacyProject.pharmacy.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.PharmacyProject.pharmacy.entity.Pharmacy;
import study.PharmacyProject.pharmacy.repository.PharmacyRepository;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PharmacyRepositoryService {
    private final PharmacyRepository pharmacyRepository;

    //약국 주소 변경
    @Transactional
    public void updateAddress(Long id,String address){
        Pharmacy findPharmacy = pharmacyRepository.findById(id).orElse(null);
        if(Objects.isNull(findPharmacy)){
            log.info("[PharmacyRepositoryService.updateAddress] not found id:{}",id);
            return;
        }
        findPharmacy.changePharmacyAddress(address);
    }

    public List<Pharmacy> findAll(){
        return pharmacyRepository.findAll();
    }
}
