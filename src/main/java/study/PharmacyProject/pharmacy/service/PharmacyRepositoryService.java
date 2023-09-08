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
@RequiredArgsConstructor
public class PharmacyRepositoryService {
    private final PharmacyRepository pharmacyRepository;

    @Transactional
    public void updateAddress(Long id,String address){
        Pharmacy entity = pharmacyRepository.findById(id).orElse(null);
        if(Objects.isNull(entity)){
            log.info("[PharmacyRepositoryService.updateAddress] not found id:{}",id);
            return;
        }
        entity.changePharmacyAddress(address);
    }


    ///업데이트안되지 jpa 하나의 트랜잭션안에서동작한 커밋해야 반영된다ㅎㅎ까먹지말자
    @Transactional(readOnly = true)
    public void updateAddressWithoutTransaction(Long id,String address){
        Pharmacy entity = pharmacyRepository.findById(id).orElse(null);
        if(Objects.isNull(entity)){
            log.info("[PharmacyRepositoryService.updateAddress] not found id:{}",id);
            return;
        }
        entity.changePharmacyAddress(address);
    }


    public List<Pharmacy> findAll(){
        return pharmacyRepository.findAll();
    }
}
