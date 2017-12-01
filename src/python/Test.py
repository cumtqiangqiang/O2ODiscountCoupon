import pandas as pd
import  Constants
if __name__ == '__main__':
    df = pd.read_csv(Constants.TRAIN_OFFLINE_DATA_PATH)
    valid_time_user = df.loc[df['Coupon_id'].notnull()].loc[df['Date'].notnull()].loc[df['Discount_rate'].notnull()]
    print valid_time_user.iloc[:,0].size

    print '----------------------------'

    valid_time_user1 = df.loc[df['Coupon_id'].notnull()].loc[df['Date'].notnull()]
    print valid_time_user1.iloc[:,0].size
