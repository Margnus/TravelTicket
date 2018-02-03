package com.travel.ticket.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lixiaofan on 2018/2/3.
 */

public class PortResult {
    /**
     * corporationId : string
     * description : string
     * id : string
     * name : string
     * port : {"city":{"cityId":"string","country":{"ename":"string","id":"string","name":"string","shortName":"string"},"ename":"string","name":"string"},"cityId":"string","description":"string","id":"string","name":"string"}
     * portCorporation : {"address":"string","aptitudeMaterial":"string","bank":"string","bankCardNumber":"string","bankUserName":"string","cityId":"string","cname":"string","ename":"string","financial":"string","financialEmail":"string","financialFax":"string","financialMobile":"string","financialTel":"string","id":"string","pricipalTel":"string","principal":"string","principalEmail":"string","principalFax":"string","principalMobile":"string","remark":"string","status":"enable"}
     * portId : string
     * status : enable
     */

    @SerializedName("corporationId")
    private String corporationId;
    @SerializedName("description")
    private String description;
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("port")
    private PortBean port;
    @SerializedName("portCorporation")
    private PortCorporationBean portCorporation;
    @SerializedName("portId")
    private String portId;
    @SerializedName("status")
    private String status;

    public String getCorporationId() {
        return corporationId;
    }

    public void setCorporationId(String corporationId) {
        this.corporationId = corporationId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PortBean getPort() {
        return port;
    }

    public void setPort(PortBean port) {
        this.port = port;
    }

    public PortCorporationBean getPortCorporation() {
        return portCorporation;
    }

    public void setPortCorporation(PortCorporationBean portCorporation) {
        this.portCorporation = portCorporation;
    }

    public String getPortId() {
        return portId;
    }

    public void setPortId(String portId) {
        this.portId = portId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class PortBean {
        /**
         * city : {"cityId":"string","country":{"ename":"string","id":"string","name":"string","shortName":"string"},"ename":"string","name":"string"}
         * cityId : string
         * description : string
         * id : string
         * name : string
         */

        @SerializedName("city")
        private CityBean city;
        @SerializedName("cityId")
        private String cityId;
        @SerializedName("description")
        private String description;
        @SerializedName("id")
        private String id;
        @SerializedName("name")
        private String name;

        public CityBean getCity() {
            return city;
        }

        public void setCity(CityBean city) {
            this.city = city;
        }

        public String getCityId() {
            return cityId;
        }

        public void setCityId(String cityId) {
            this.cityId = cityId;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public static class CityBean {
            /**
             * cityId : string
             * country : {"ename":"string","id":"string","name":"string","shortName":"string"}
             * ename : string
             * name : string
             */

            @SerializedName("cityId")
            private String cityId;
            @SerializedName("country")
            private CountryBean country;
            @SerializedName("ename")
            private String ename;
            @SerializedName("name")
            private String name;

            public String getCityId() {
                return cityId;
            }

            public void setCityId(String cityId) {
                this.cityId = cityId;
            }

            public CountryBean getCountry() {
                return country;
            }

            public void setCountry(CountryBean country) {
                this.country = country;
            }

            public String getEname() {
                return ename;
            }

            public void setEname(String ename) {
                this.ename = ename;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public static class CountryBean {
                /**
                 * ename : string
                 * id : string
                 * name : string
                 * shortName : string
                 */

                @SerializedName("ename")
                private String ename;
                @SerializedName("id")
                private String id;
                @SerializedName("name")
                private String name;
                @SerializedName("shortName")
                private String shortName;

                public String getEname() {
                    return ename;
                }

                public void setEname(String ename) {
                    this.ename = ename;
                }

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getShortName() {
                    return shortName;
                }

                public void setShortName(String shortName) {
                    this.shortName = shortName;
                }
            }
        }
    }

    public static class PortCorporationBean {
        /**
         * address : string
         * aptitudeMaterial : string
         * bank : string
         * bankCardNumber : string
         * bankUserName : string
         * cityId : string
         * cname : string
         * ename : string
         * financial : string
         * financialEmail : string
         * financialFax : string
         * financialMobile : string
         * financialTel : string
         * id : string
         * pricipalTel : string
         * principal : string
         * principalEmail : string
         * principalFax : string
         * principalMobile : string
         * remark : string
         * status : enable
         */

        @SerializedName("address")
        private String address;
        @SerializedName("aptitudeMaterial")
        private String aptitudeMaterial;
        @SerializedName("bank")
        private String bank;
        @SerializedName("bankCardNumber")
        private String bankCardNumber;
        @SerializedName("bankUserName")
        private String bankUserName;
        @SerializedName("cityId")
        private String cityId;
        @SerializedName("cname")
        private String cname;
        @SerializedName("ename")
        private String ename;
        @SerializedName("financial")
        private String financial;
        @SerializedName("financialEmail")
        private String financialEmail;
        @SerializedName("financialFax")
        private String financialFax;
        @SerializedName("financialMobile")
        private String financialMobile;
        @SerializedName("financialTel")
        private String financialTel;
        @SerializedName("id")
        private String id;
        @SerializedName("pricipalTel")
        private String pricipalTel;
        @SerializedName("principal")
        private String principal;
        @SerializedName("principalEmail")
        private String principalEmail;
        @SerializedName("principalFax")
        private String principalFax;
        @SerializedName("principalMobile")
        private String principalMobile;
        @SerializedName("remark")
        private String remark;
        @SerializedName("status")
        private String status;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getAptitudeMaterial() {
            return aptitudeMaterial;
        }

        public void setAptitudeMaterial(String aptitudeMaterial) {
            this.aptitudeMaterial = aptitudeMaterial;
        }

        public String getBank() {
            return bank;
        }

        public void setBank(String bank) {
            this.bank = bank;
        }

        public String getBankCardNumber() {
            return bankCardNumber;
        }

        public void setBankCardNumber(String bankCardNumber) {
            this.bankCardNumber = bankCardNumber;
        }

        public String getBankUserName() {
            return bankUserName;
        }

        public void setBankUserName(String bankUserName) {
            this.bankUserName = bankUserName;
        }

        public String getCityId() {
            return cityId;
        }

        public void setCityId(String cityId) {
            this.cityId = cityId;
        }

        public String getCname() {
            return cname;
        }

        public void setCname(String cname) {
            this.cname = cname;
        }

        public String getEname() {
            return ename;
        }

        public void setEname(String ename) {
            this.ename = ename;
        }

        public String getFinancial() {
            return financial;
        }

        public void setFinancial(String financial) {
            this.financial = financial;
        }

        public String getFinancialEmail() {
            return financialEmail;
        }

        public void setFinancialEmail(String financialEmail) {
            this.financialEmail = financialEmail;
        }

        public String getFinancialFax() {
            return financialFax;
        }

        public void setFinancialFax(String financialFax) {
            this.financialFax = financialFax;
        }

        public String getFinancialMobile() {
            return financialMobile;
        }

        public void setFinancialMobile(String financialMobile) {
            this.financialMobile = financialMobile;
        }

        public String getFinancialTel() {
            return financialTel;
        }

        public void setFinancialTel(String financialTel) {
            this.financialTel = financialTel;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPricipalTel() {
            return pricipalTel;
        }

        public void setPricipalTel(String pricipalTel) {
            this.pricipalTel = pricipalTel;
        }

        public String getPrincipal() {
            return principal;
        }

        public void setPrincipal(String principal) {
            this.principal = principal;
        }

        public String getPrincipalEmail() {
            return principalEmail;
        }

        public void setPrincipalEmail(String principalEmail) {
            this.principalEmail = principalEmail;
        }

        public String getPrincipalFax() {
            return principalFax;
        }

        public void setPrincipalFax(String principalFax) {
            this.principalFax = principalFax;
        }

        public String getPrincipalMobile() {
            return principalMobile;
        }

        public void setPrincipalMobile(String principalMobile) {
            this.principalMobile = principalMobile;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
