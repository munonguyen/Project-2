-- MySQL seed data for checking pagination on /admin/buildings/list
-- Target database: estateadvance
-- Usage:
--   mysql -u root -p estateadvance < src/main/resources/sql/building-pagination-seed.sql

START TRANSACTION;

INSERT INTO building (
    createddate,
    createdby,
    modifieddate,
    modifiedby,
    name,
    street,
    ward,
    district,
    structure,
    numberofbasement,
    floorarea,
    direction,
    level,
    rentprice,
    rentpricedescription,
    servicefee,
    carfee,
    overtimefee,
    brokeragefee,
    type,
    note,
    motofee,
    waterfee,
    electricityfee,
    deposit,
    payment,
    renttime,
    decorationtime,
    linkofbuilding,
    map,
    managername,
    managerphone,
    image
) VALUES
    ('2026-07-01 08:00:00', 'seed-script', NULL, NULL, 'Seed Tower 01', '101 Nguyen Hue', 'Ben Nghe', 'QUAN_1', '1 tret 12 lau', 2, 420, 'Dong', 'A', 1800, 'Gia da bao gom phi co ban', '25 USD/m2', '1200000', '250000', 180.00, 'TANG_TRET,NOI_THAT', 'Seed data for pagination test', '150000', '30000', '4200', '3 thang', 'Thang', '3 nam', '14 ngay', 'https://example.com/building/01', 'https://maps.example.com/01', 'Manager 01', '0901000001', NULL),
    ('2026-07-01 08:05:00', 'seed-script', NULL, NULL, 'Seed Tower 02', '15 Le Thanh Ton', 'Ben Nghe', 'QUAN_1', '1 tret 10 lau', 1, 360, 'Tay', 'B', 1500, 'Van phong trung tam quan 1', '22 USD/m2', '1000000', '200000', 150.00, 'NGUYEN_CAN', 'Seed data for pagination test', '120000', '28000', '4100', '2 thang', 'Quy', '2 nam', '10 ngay', 'https://example.com/building/02', 'https://maps.example.com/02', 'Manager 02', '0901000002', NULL),
    ('2026-07-01 08:10:00', 'seed-script', NULL, NULL, 'Seed Tower 03', '88 Tran Hung Dao', 'Co Giang', 'QUAN_1', '1 tret 8 lau', 1, 300, 'Nam', 'C', 1200, 'Phu hop startup va SME', '18 USD/m2', '900000', '150000', 120.00, 'NOI_THAT', 'Seed data for pagination test', '100000', '25000', '3900', '2 thang', 'Thang', '2 nam', '7 ngay', 'https://example.com/building/03', 'https://maps.example.com/03', 'Manager 03', '0901000003', NULL),
    ('2026-07-01 08:15:00', 'seed-script', NULL, NULL, 'Seed Tower 04', '12 Xa Lo Ha Noi', 'An Phu', 'QUAN_2', '1 tret 15 lau', 3, 650, 'Bac', 'A', 2200, 'Toa nha moi tai khu dong', '28 USD/m2', '1500000', '300000', 220.00, 'TANG_TRET,NGUYEN_CAN', 'Seed data for pagination test', '180000', '35000', '4500', '3 thang', 'Thang', '5 nam', '20 ngay', 'https://example.com/building/04', 'https://maps.example.com/04', 'Manager 04', '0901000004', NULL),
    ('2026-07-01 08:20:00', 'seed-script', NULL, NULL, 'Seed Tower 05', '26 Mai Chi Tho', 'Thu Thiem', 'QUAN_2', '1 tret 18 lau', 3, 800, 'Dong Nam', 'A', 2600, 'View song va khu do thi moi', '32 USD/m2', '1800000', '350000', 260.00, 'NOI_THAT,NGUYEN_CAN', 'Seed data for pagination test', '200000', '38000', '4700', '4 thang', 'Quy', '5 nam', '25 ngay', 'https://example.com/building/05', 'https://maps.example.com/05', 'Manager 05', '0901000005', NULL),
    ('2026-07-01 08:25:00', 'seed-script', NULL, NULL, 'Seed Tower 06', '99 Tran Nao', 'Binh An', 'QUAN_2', '1 tret 9 lau', 2, 340, 'Tay Bac', 'B', 1450, 'Phu hop van phong dich vu', '20 USD/m2', '950000', '180000', 145.00, 'TANG_TRET', 'Seed data for pagination test', '110000', '26000', '4000', '2 thang', 'Thang', '3 nam', '10 ngay', 'https://example.com/building/06', 'https://maps.example.com/06', 'Manager 06', '0901000006', NULL),
    ('2026-07-01 08:30:00', 'seed-script', NULL, NULL, 'Seed Tower 07', '44 Dien Bien Phu', 'Vo Thi Sau', 'QUAN_3', '1 tret 11 lau', 2, 410, 'Dong Bac', 'A', 1750, 'Gan trung tam hanh chinh', '24 USD/m2', '1100000', '220000', 175.00, 'NGUYEN_CAN,NOI_THAT', 'Seed data for pagination test', '130000', '30000', '4300', '3 thang', 'Thang', '3 nam', '12 ngay', 'https://example.com/building/07', 'https://maps.example.com/07', 'Manager 07', '0901000007', NULL),
    ('2026-07-01 08:35:00', 'seed-script', NULL, NULL, 'Seed Tower 08', '75 Nam Ky Khoi Nghia', 'Ward 7', 'QUAN_3', '1 tret 13 lau', 2, 500, 'Tay Nam', 'A', 2100, 'Khu vuc co ket noi tot', '27 USD/m2', '1400000', '260000', 210.00, 'TANG_TRET,NGUYEN_CAN,NOI_THAT', 'Seed data for pagination test', '170000', '33000', '4450', '3 thang', 'Quy', '4 nam', '15 ngay', 'https://example.com/building/08', 'https://maps.example.com/08', 'Manager 08', '0901000008', NULL),
    ('2026-07-01 08:40:00', 'seed-script', NULL, NULL, 'Seed Tower 09', '21 Cach Mang Thang 8', 'Ward 11', 'QUAN_3', '1 tret 7 lau', 1, 280, 'Dong', 'B', 1180, 'Muc gia de tiep can', '17 USD/m2', '850000', '150000', 118.00, 'NOI_THAT', 'Seed data for pagination test', '95000', '24000', '3850', '2 thang', 'Thang', '2 nam', '7 ngay', 'https://example.com/building/09', 'https://maps.example.com/09', 'Manager 09', '0901000009', NULL),
    ('2026-07-01 08:45:00', 'seed-script', NULL, NULL, 'Seed Tower 10', '5 Ben Van Don', 'Ward 12', 'QUAN_4', '1 tret 12 lau', 2, 430, 'Nam', 'B', 1680, 'Gan cau Khanh Hoi', '23 USD/m2', '1050000', '210000', 168.00, 'NGUYEN_CAN', 'Seed data for pagination test', '125000', '29000', '4200', '2 thang', 'Thang', '3 nam', '10 ngay', 'https://example.com/building/10', 'https://maps.example.com/10', 'Manager 10', '0901000010', NULL),
    ('2026-07-01 08:50:00', 'seed-script', NULL, NULL, 'Seed Tower 11', '36 Hoang Dieu', 'Ward 9', 'QUAN_4', '1 tret 10 lau', 1, 350, 'Tay', 'C', 1350, 'Phu hop doanh nghiep nho', '19 USD/m2', '920000', '170000', 135.00, 'TANG_TRET,NOI_THAT', 'Seed data for pagination test', '105000', '25000', '3980', '2 thang', 'Thang', '2 nam', '9 ngay', 'https://example.com/building/11', 'https://maps.example.com/11', 'Manager 11', '0901000011', NULL),
    ('2026-07-01 08:55:00', 'seed-script', NULL, NULL, 'Seed Tower 12', '92 Nguyen Trai', 'Ward 2', 'QUAN_5', '1 tret 14 lau', 2, 540, 'Dong Nam', 'A', 2050, 'Khu vuc kinh doanh sam uat', '26 USD/m2', '1350000', '240000', 205.00, 'NGUYEN_CAN,NOI_THAT', 'Seed data for pagination test', '160000', '31000', '4380', '3 thang', 'Quy', '4 nam', '14 ngay', 'https://example.com/building/12', 'https://maps.example.com/12', 'Manager 12', '0901000012', NULL),
    ('2026-07-01 09:00:00', 'seed-script', NULL, NULL, 'Seed Tower 13', '60 Tran Phu', 'Ward 4', 'QUAN_5', '1 tret 9 lau', 1, 320, 'Bac', 'B', 1420, 'Gia can bang va de van hanh', '20 USD/m2', '980000', '175000', 142.00, 'TANG_TRET', 'Seed data for pagination test', '115000', '26500', '4040', '2 thang', 'Thang', '3 nam', '10 ngay', 'https://example.com/building/13', 'https://maps.example.com/13', 'Manager 13', '0901000013', NULL),
    ('2026-07-01 09:05:00', 'seed-script', NULL, NULL, 'Seed Tower 14', '140 Ba Thang Hai', 'Ward 12', 'QUAN_10', '1 tret 16 lau', 3, 700, 'Dong Bac', 'A', 2450, 'Toa nha lon cho doanh nghiep', '30 USD/m2', '1700000', '320000', 245.00, 'TANG_TRET,NGUYEN_CAN', 'Seed data for pagination test', '185000', '36000', '4620', '4 thang', 'Quy', '5 nam', '20 ngay', 'https://example.com/building/14', 'https://maps.example.com/14', 'Manager 14', '0901000014', NULL),
    ('2026-07-01 09:10:00', 'seed-script', NULL, NULL, 'Seed Tower 15', '28 Su Van Hanh', 'Ward 10', 'QUAN_10', '1 tret 12 lau', 2, 460, 'Tay Nam', 'B', 1880, 'Khu y te va giao duc', '24 USD/m2', '1180000', '225000', 188.00, 'NOI_THAT', 'Seed data for pagination test', '145000', '29500', '4250', '3 thang', 'Thang', '3 nam', '12 ngay', 'https://example.com/building/15', 'https://maps.example.com/15', 'Manager 15', '0901000015', NULL),
    ('2026-07-01 09:15:00', 'seed-script', NULL, NULL, 'Seed Tower 16', '70 Le Dai Hanh', 'Ward 15', 'QUAN_11', '1 tret 8 lau', 1, 310, 'Dong', 'C', 1260, 'Van phong gia hop ly', '18 USD/m2', '870000', '160000', 126.00, 'NGUYEN_CAN', 'Seed data for pagination test', '98000', '24500', '3900', '2 thang', 'Thang', '2 nam', '8 ngay', 'https://example.com/building/16', 'https://maps.example.com/16', 'Manager 16', '0901000016', NULL),
    ('2026-07-01 09:20:00', 'seed-script', NULL, NULL, 'Seed Tower 17', '133 Lac Long Quan', 'Ward 5', 'QUAN_11', '1 tret 10 lau', 2, 370, 'Tay', 'B', 1520, 'Phu hop trung tam dich vu', '21 USD/m2', '990000', '185000', 152.00, 'TANG_TRET,NOI_THAT', 'Seed data for pagination test', '120000', '27000', '4080', '2 thang', 'Thang', '3 nam', '10 ngay', 'https://example.com/building/17', 'https://maps.example.com/17', 'Manager 17', '0901000017', NULL),
    ('2026-07-01 09:25:00', 'seed-script', NULL, NULL, 'Seed Tower 18', '55 Nguyen Van Qua', 'Dong Hung Thuan', 'QUAN_12', '1 tret 11 lau', 2, 440, 'Nam', 'B', 1650, 'Khu phat trien moi phia tay bac', '22 USD/m2', '1080000', '205000', 165.00, 'NOI_THAT,NGUYEN_CAN', 'Seed data for pagination test', '132000', '28500', '4180', '3 thang', 'Quy', '3 nam', '12 ngay', 'https://example.com/building/18', 'https://maps.example.com/18', 'Manager 18', '0901000018', NULL),
    ('2026-07-01 09:30:00', 'seed-script', NULL, NULL, 'Seed Tower 19', '19 Le Thi Rieng', 'Thanh Xuan', 'QUAN_12', '1 tret 9 lau', 1, 295, 'Dong Nam', 'C', 1190, 'Muc gia de test filter', '17 USD/m2', '820000', '145000', 119.00, 'TANG_TRET', 'Seed data for pagination test', '93000', '23500', '3820', '2 thang', 'Thang', '2 nam', '7 ngay', 'https://example.com/building/19', 'https://maps.example.com/19', 'Manager 19', '0901000019', NULL),
    ('2026-07-01 09:35:00', 'seed-script', NULL, NULL, 'Seed Tower 20', '210 To Ky', 'Trung My Tay', 'QUAN_12', '1 tret 13 lau', 2, 520, 'Bac', 'A', 1980, 'Toa nha cuoi cung de vuot qua 2 trang', '25 USD/m2', '1250000', '235000', 198.00, 'TANG_TRET,NGUYEN_CAN,NOI_THAT', 'Seed data for pagination test', '155000', '30500', '4350', '3 thang', 'Quy', '4 nam', '15 ngay', 'https://example.com/building/20', 'https://maps.example.com/20', 'Manager 20', '0901000020', NULL);

INSERT INTO rentarea (createddate, createdby, modifieddate, modifiedby, value, buildingid)
SELECT '2026-07-01 10:00:00', 'seed-script', NULL, NULL, 120, id FROM building WHERE name = 'Seed Tower 01'
UNION ALL SELECT '2026-07-01 10:00:00', 'seed-script', NULL, NULL, 180, id FROM building WHERE name = 'Seed Tower 01'
UNION ALL SELECT '2026-07-01 10:00:00', 'seed-script', NULL, NULL, 90, id FROM building WHERE name = 'Seed Tower 02'
UNION ALL SELECT '2026-07-01 10:00:00', 'seed-script', NULL, NULL, 140, id FROM building WHERE name = 'Seed Tower 02'
UNION ALL SELECT '2026-07-01 10:00:00', 'seed-script', NULL, NULL, 80, id FROM building WHERE name = 'Seed Tower 03'
UNION ALL SELECT '2026-07-01 10:00:00', 'seed-script', NULL, NULL, 120, id FROM building WHERE name = 'Seed Tower 03'
UNION ALL SELECT '2026-07-01 10:00:00', 'seed-script', NULL, NULL, 160, id FROM building WHERE name = 'Seed Tower 04'
UNION ALL SELECT '2026-07-01 10:00:00', 'seed-script', NULL, NULL, 240, id FROM building WHERE name = 'Seed Tower 04'
UNION ALL SELECT '2026-07-01 10:00:00', 'seed-script', NULL, NULL, 200, id FROM building WHERE name = 'Seed Tower 05'
UNION ALL SELECT '2026-07-01 10:00:00', 'seed-script', NULL, NULL, 320, id FROM building WHERE name = 'Seed Tower 05'
UNION ALL SELECT '2026-07-01 10:00:00', 'seed-script', NULL, NULL, 100, id FROM building WHERE name = 'Seed Tower 06'
UNION ALL SELECT '2026-07-01 10:00:00', 'seed-script', NULL, NULL, 150, id FROM building WHERE name = 'Seed Tower 06'
UNION ALL SELECT '2026-07-01 10:00:00', 'seed-script', NULL, NULL, 130, id FROM building WHERE name = 'Seed Tower 07'
UNION ALL SELECT '2026-07-01 10:00:00', 'seed-script', NULL, NULL, 190, id FROM building WHERE name = 'Seed Tower 07'
UNION ALL SELECT '2026-07-01 10:00:00', 'seed-script', NULL, NULL, 150, id FROM building WHERE name = 'Seed Tower 08'
UNION ALL SELECT '2026-07-01 10:00:00', 'seed-script', NULL, NULL, 210, id FROM building WHERE name = 'Seed Tower 08'
UNION ALL SELECT '2026-07-01 10:00:00', 'seed-script', NULL, NULL, 70, id FROM building WHERE name = 'Seed Tower 09'
UNION ALL SELECT '2026-07-01 10:00:00', 'seed-script', NULL, NULL, 110, id FROM building WHERE name = 'Seed Tower 09'
UNION ALL SELECT '2026-07-01 10:00:00', 'seed-script', NULL, NULL, 120, id FROM building WHERE name = 'Seed Tower 10'
UNION ALL SELECT '2026-07-01 10:00:00', 'seed-script', NULL, NULL, 180, id FROM building WHERE name = 'Seed Tower 10'
UNION ALL SELECT '2026-07-01 10:00:00', 'seed-script', NULL, NULL, 90, id FROM building WHERE name = 'Seed Tower 11'
UNION ALL SELECT '2026-07-01 10:00:00', 'seed-script', NULL, NULL, 130, id FROM building WHERE name = 'Seed Tower 11'
UNION ALL SELECT '2026-07-01 10:00:00', 'seed-script', NULL, NULL, 140, id FROM building WHERE name = 'Seed Tower 12'
UNION ALL SELECT '2026-07-01 10:00:00', 'seed-script', NULL, NULL, 220, id FROM building WHERE name = 'Seed Tower 12'
UNION ALL SELECT '2026-07-01 10:00:00', 'seed-script', NULL, NULL, 95, id FROM building WHERE name = 'Seed Tower 13'
UNION ALL SELECT '2026-07-01 10:00:00', 'seed-script', NULL, NULL, 145, id FROM building WHERE name = 'Seed Tower 13'
UNION ALL SELECT '2026-07-01 10:00:00', 'seed-script', NULL, NULL, 180, id FROM building WHERE name = 'Seed Tower 14'
UNION ALL SELECT '2026-07-01 10:00:00', 'seed-script', NULL, NULL, 260, id FROM building WHERE name = 'Seed Tower 14'
UNION ALL SELECT '2026-07-01 10:00:00', 'seed-script', NULL, NULL, 120, id FROM building WHERE name = 'Seed Tower 15'
UNION ALL SELECT '2026-07-01 10:00:00', 'seed-script', NULL, NULL, 170, id FROM building WHERE name = 'Seed Tower 15'
UNION ALL SELECT '2026-07-01 10:00:00', 'seed-script', NULL, NULL, 85, id FROM building WHERE name = 'Seed Tower 16'
UNION ALL SELECT '2026-07-01 10:00:00', 'seed-script', NULL, NULL, 125, id FROM building WHERE name = 'Seed Tower 16'
UNION ALL SELECT '2026-07-01 10:00:00', 'seed-script', NULL, NULL, 100, id FROM building WHERE name = 'Seed Tower 17'
UNION ALL SELECT '2026-07-01 10:00:00', 'seed-script', NULL, NULL, 155, id FROM building WHERE name = 'Seed Tower 17'
UNION ALL SELECT '2026-07-01 10:00:00', 'seed-script', NULL, NULL, 115, id FROM building WHERE name = 'Seed Tower 18'
UNION ALL SELECT '2026-07-01 10:00:00', 'seed-script', NULL, NULL, 175, id FROM building WHERE name = 'Seed Tower 18'
UNION ALL SELECT '2026-07-01 10:00:00', 'seed-script', NULL, NULL, 75, id FROM building WHERE name = 'Seed Tower 19'
UNION ALL SELECT '2026-07-01 10:00:00', 'seed-script', NULL, NULL, 105, id FROM building WHERE name = 'Seed Tower 19'
UNION ALL SELECT '2026-07-01 10:00:00', 'seed-script', NULL, NULL, 140, id FROM building WHERE name = 'Seed Tower 20'
UNION ALL SELECT '2026-07-01 10:00:00', 'seed-script', NULL, NULL, 210, id FROM building WHERE name = 'Seed Tower 20';

COMMIT;
