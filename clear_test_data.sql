DELETE FROM auction.BILLING WHERE id > 0;
DELETE FROM auction.bid WHERE id > 0;


UPDATE  auction.PRODUCT
SET status = 'RELEASE'
WHERE id=1
;

DELETE FROM auction.PRODUCT WHERE id > 0;
INSERT INTO PRODUCT (`id`, `bid_due_date`, `bid_start_price`, `condition_of_sale`, `created`, `deposit`, `description`, `name`, `owner`, `payment_due_date`, `shipping_information`, `status`) values (1, '2023-10-22 08:24:00.000000', '100', 'New', '2023-10-21 08:01:44.638000', '50', 'Iphone 15 Ultra', 'Iphone 15 Ultra', 'seller@gmail.com', '2023-10-22 00:00:00.000000', 'Iowa', 'RELEASE');
INSERT INTO PRODUCT (`id`, `bid_due_date`, `bid_start_price`, `condition_of_sale`, `created`, `deposit`, `description`, `name`, `owner`, `payment_due_date`, `shipping_information`, `status`) values (2, '2023-10-22 08:25:00.000000', '200', 'New', '2023-10-21 08:01:44.638000', '50', 'Macbook Pro 17 inches', 'Macbook Pro 17 inches', 'seller@gmail.com', '2023-10-22 00:00:00.000000', 'Iowa', 'RELEASE');


update user
set current_balance = 10000 where id > 0;

UPDATE USER
SET CURRENT_BALANCE = 9940 WHERE ID =3;

UPDATE USER
SET CURRENT_BALANCE = 9950 WHERE ID =4;

INSERT INTO BILLING  (`amount`, `balance`, `details`, `transaction_date`, `type`, `user_id`) VALUES ('60', '9940', 'Deposit for product 1', '2023-10-22 08:22:22.990000', 'Debit', '3');
INSERT INTO BILLING  (`amount`, `balance`, `details`, `transaction_date`, `type`, `user_id`) VALUES ('50', '9950', 'Deposit for product 1', '2023-10-22 08:23:09.368000', 'Debit', '4');

INSERT INTO BID (`bid_date`, `bid_price`, `deposit`, `deposit_date`, `winner`, `product_id`, `user_id`, `status`) VALUES ( NULL, '0', '60', '2023-10-22 08:22:22.990000', 0, '1', '3', 'ACTIVE');
INSERT INTO BID (`bid_date`, `bid_price`, `deposit`, `deposit_date`, `winner`, `product_id`, `user_id`, `status`) VALUES ('2023-10-22 08:22:28.401000', '110', '0', NULL, 0, '1', '3', 'ACTIVE');
INSERT INTO BID (`bid_date`, `bid_price`, `deposit`, `deposit_date`, `winner`, `product_id`, `user_id`, `status`) VALUES (NULL, '0', '50', '2023-10-22 08:23:09.368000', 0, '1', '4', 'ACTIVE');
INSERT INTO BID (`bid_date`, `bid_price`, `deposit`, `deposit_date`, `winner`, `product_id`, `user_id`, `status`) VALUES ('2023-10-22 08:23:13.983000', '120', '0', NULL, 0, '1', '4', 'ACTIVE');
INSERT INTO BID (`bid_date`, `bid_price`, `deposit`, `deposit_date`, `winner`, `product_id`, `user_id`, `status`) VALUES ('2023-10-22 08:23:26.340000', '130', '0', NULL, 0, '1', '3', 'ACTIVE');
INSERT INTO BID (`bid_date`, `bid_price`, `deposit`, `deposit_date`, `winner`, `product_id`, `user_id`, `status`) VALUES ('2023-10-22 08:23:30.204000', '140', '0', NULL, 0, '1', '4', 'ACTIVE');



