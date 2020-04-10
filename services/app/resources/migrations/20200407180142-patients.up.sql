CREATE TABLE IF NOT EXISTS patients (
  id    SERIAL PRIMARY KEY,
  full_name  varchar(255) NOT NULL,
  date_of_birth date,
  sex varchar(255) NOT NULL,
  address varchar(255) NOT NULL,
  health_insurance_number varchar(255) NOT NULL,
  timestamp timestamp DEFAULT current_timestamp
);
--;;
CREATE UNIQUE INDEX index_patients_health_insurance_number ON patients(health_insurance_number);
