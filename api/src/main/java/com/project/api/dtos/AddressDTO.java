package com.project.api.dtos;


import com.project.api.entities.Address;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddressDTO {
    private Integer addressId;
    private String roadName;
    private WardDTO ward;
    private DistrictDTO district;
    private ProvinceDTO province;

    public AddressDTO(Address address) {
        this.addressId = address.getAddressId();
        this.roadName = address.getRoadName();
        this.ward = new WardDTO(address.getWard());
        this.district = new DistrictDTO(address.getDistrict());
        this.province = new ProvinceDTO(address.getProvince());
    }
}
