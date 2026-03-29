CREATE TABLE artefact (
    artefact_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    material VARCHAR(50),
    description TEXT,
    discovered_year INT,
    image_path VARCHAR(255),
    category_id INT,
    period_id INT,
    region_id INT,

    FOREIGN KEY (category_id) REFERENCES category(category_id),
    FOREIGN KEY (period_id) REFERENCES period(period_id),
    FOREIGN KEY (region_id) REFERENCES region(region_id)
);