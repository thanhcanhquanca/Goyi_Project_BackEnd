
-- Tạo cơ sở dữ liệu
CREATE DATABASE IF NOT EXISTS goyi_management;
USE goyi_management;

-- Tạo bảng roles
CREATE TABLE roles (
                       role_id INT PRIMARY KEY AUTO_INCREMENT,
                       role_name VARCHAR(50) NOT NULL UNIQUE,
                       description TEXT,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Tạo bảng permissions
CREATE TABLE permissions (
                             permission_id INT PRIMARY KEY AUTO_INCREMENT,
                             permission_name VARCHAR(100) NOT NULL UNIQUE,
                             description TEXT,
                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tạo bảng paid_features
CREATE TABLE paid_features (
                               feature_id INT PRIMARY KEY AUTO_INCREMENT,
                               feature_name VARCHAR(255) NOT NULL UNIQUE,
                               description TEXT,
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tạo bảng subscription_plans
CREATE TABLE subscription_plans (
                                    plan_id INT PRIMARY KEY AUTO_INCREMENT,
                                    plan_name VARCHAR(255) NOT NULL UNIQUE,
                                    description TEXT,
                                    price DECIMAL(10,2) NOT NULL,
                                    duration INT NOT NULL,
                                    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
                                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tạo bảng video_categories
CREATE TABLE video_categories (
                                  category_id INT PRIMARY KEY AUTO_INCREMENT,
                                  category_name VARCHAR(255) NOT NULL UNIQUE,
                                  description TEXT,
                                  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tạo bảng video_tags
CREATE TABLE video_tags (
                            tag_id INT PRIMARY KEY AUTO_INCREMENT,
                            tag_name VARCHAR(255) NOT NULL UNIQUE,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tạo bảng users
CREATE TABLE users (
                       user_id INT PRIMARY KEY AUTO_INCREMENT,
                       user_name VARCHAR(50) NOT NULL UNIQUE,
                       phone_number VARCHAR(20) NOT NULL UNIQUE,
                       email VARCHAR(100),
                       password VARCHAR(255) NOT NULL,
                       roleid INT NOT NULL,
                       status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                       last_login TIMESTAMP,
                       address TEXT,
                       profile_picture VARCHAR(255),
                       cover_photo VARCHAR(255),
                       qr_code VARCHAR(255),
                       user_code VARCHAR(50) UNIQUE,
                       is2fa_enabled TINYINT NOT NULL DEFAULT 0,
                       twofasecret VARCHAR(100),
                       copyright_violations INT NOT NULL DEFAULT 0,
                       FOREIGN KEY (roleid) REFERENCES roles(role_id)
);

-- Tạo bảng admins
CREATE TABLE admins (
                        admin_id INT PRIMARY KEY AUTO_INCREMENT,
                        user_name VARCHAR(50) NOT NULL UNIQUE,
                        phone_number VARCHAR(20) NOT NULL UNIQUE,
                        email VARCHAR(100),
                        password VARCHAR(255) NOT NULL,
                        status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Tạo bảng videos
CREATE TABLE videos (
                        video_id INT PRIMARY KEY AUTO_INCREMENT,
                        userid INT NOT NULL,
                        title VARCHAR(255) NOT NULL,
                        description TEXT,
                        video_url VARCHAR(500) NOT NULL,
                        thumbnail_url VARCHAR(500),
                        duration INT,
                        status VARCHAR(50) NOT NULL DEFAULT 'PUBLIC',
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        is_deleted TINYINT NOT NULL DEFAULT 0,
                        copyright_owner VARCHAR(255),
                        copyright_license VARCHAR(255),
                        copyright_issue_date DATE,
                        copyright_expiry_date DATE,
                        copyright_status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
                        available_qualities TEXT,
                        FOREIGN KEY (userid) REFERENCES users(user_id)
);

-- Tạo bảng admin_ad_campaigns
CREATE TABLE admin_ad_campaigns (
                                    campaign_id INT PRIMARY KEY AUTO_INCREMENT,
                                    adminid INT NOT NULL,
                                    campaign_title VARCHAR(255) NOT NULL,
                                    campaign_content TEXT NOT NULL,
                                    description TEXT,
                                    target_location VARCHAR(50) NOT NULL,
                                    start_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                    end_date TIMESTAMP,
                                    status VARCHAR(50) NOT NULL DEFAULT 'INACTIVE',
                                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                    FOREIGN KEY (adminid) REFERENCES admins(admin_id)
);

-- Tạo bảng ad_campaigns
CREATE TABLE ad_campaigns (
                              campaign_id INT PRIMARY KEY AUTO_INCREMENT,
                              userid INT NOT NULL,
                              campaign_name VARCHAR(255) NOT NULL,
                              description TEXT,
                              budget DECIMAL(10,2) NOT NULL,
                              duration INT NOT NULL,
                              target_views INT NOT NULL DEFAULT 0,
                              target_subscriptions INT NOT NULL DEFAULT 0,
                              views_achieved INT NOT NULL DEFAULT 0,
                              subscriptions_achieved INT NOT NULL DEFAULT 0,
                              start_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              end_date TIMESTAMP,
                              status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                              FOREIGN KEY (userid) REFERENCES users(user_id)
);

-- Tạo bảng ad_display_locations
CREATE TABLE ad_display_locations (
                                      location_id INT PRIMARY KEY AUTO_INCREMENT,
                                      campaignid INT NOT NULL,
                                      location_name VARCHAR(255) NOT NULL,
                                      location_description TEXT,
                                      display_start TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                      display_end TIMESTAMP,
                                      FOREIGN KEY (campaignid) REFERENCES admin_ad_campaigns(campaign_id)
);

-- Tạo bảng ad_impressions
CREATE TABLE ad_impressions (
                                impression_id INT PRIMARY KEY AUTO_INCREMENT,
                                campaignid INT NOT NULL,
                                userid INT NOT NULL,
                                impression_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                FOREIGN KEY (campaignid) REFERENCES ad_campaigns(campaign_id),
                                FOREIGN KEY (userid) REFERENCES users(user_id)
);

-- Tạo bảng admin_activity_logs
CREATE TABLE admin_activity_logs (
                                     log_id INT PRIMARY KEY AUTO_INCREMENT,
                                     adminid INT NOT NULL,
                                     action_type VARCHAR(50) NOT NULL,
                                     action_details TEXT,
                                     action_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                     FOREIGN KEY (adminid) REFERENCES admins(admin_id)
);

-- Tạo bảng ad_placements
CREATE TABLE ad_placements (
                               placement_id INT PRIMARY KEY AUTO_INCREMENT,
                               campaignid INT NOT NULL,
                               locationid INT,
                               videoid INT,
                               display_start TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               display_end TIMESTAMP,
                               status VARCHAR(50) NOT NULL DEFAULT 'INACTIVE',
                               FOREIGN KEY (campaignid) REFERENCES ad_campaigns(campaign_id),
                               FOREIGN KEY (locationid) REFERENCES ad_display_locations(location_id),
                               FOREIGN KEY (videoid) REFERENCES videos(video_id)
);

-- Tạo bảng ad_revenue
CREATE TABLE ad_revenue (
                            revenue_id INT PRIMARY KEY AUTO_INCREMENT,
                            userid INT NOT NULL,
                            videoid INT,
                            campaignid INT,
                            total_views INT NOT NULL DEFAULT 0,
                            cpm DECIMAL(10,2) NOT NULL,
                            revenue_amount DECIMAL(10,2) NOT NULL,
                            revenue_date DATE NOT NULL,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            FOREIGN KEY (userid) REFERENCES users(user_id),
                            FOREIGN KEY (videoid) REFERENCES videos(video_id),
                            FOREIGN KEY (campaignid) REFERENCES ad_campaigns(campaign_id)
);

-- Tạo bảng ad_views
CREATE TABLE ad_views (
                          view_id INT PRIMARY KEY AUTO_INCREMENT,
                          campaignid INT NOT NULL,
                          userid INT NOT NULL,
                          view_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          FOREIGN KEY (campaignid) REFERENCES ad_campaigns(campaign_id),
                          FOREIGN KEY (userid) REFERENCES users(user_id)
);

-- Tạo bảng channel_follow_history
CREATE TABLE channel_follow_history (
                                        history_id INT PRIMARY KEY AUTO_INCREMENT,
                                        follower_channelid INT NOT NULL,
                                        followed_channelid INT NOT NULL,
                                        action VARCHAR(50) NOT NULL,
                                        action_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                        FOREIGN KEY (follower_channelid) REFERENCES users(user_id),
                                        FOREIGN KEY (followed_channelid) REFERENCES users(user_id)
);

-- Tạo bảng channel_followings
CREATE TABLE channel_followings (
                                    following_id INT PRIMARY KEY AUTO_INCREMENT,
                                    follower_channelid INT NOT NULL,
                                    followed_channelid INT NOT NULL,
                                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                    FOREIGN KEY (follower_channelid) REFERENCES users(user_id),
                                    FOREIGN KEY (followed_channelid) REFERENCES users(user_id)
);

-- Tạo bảng posts
CREATE TABLE posts (
                       post_id INT PRIMARY KEY AUTO_INCREMENT,
                       userid INT NOT NULL,
                       title VARCHAR(255) NOT NULL,
                       summary VARCHAR(500),
                       content TEXT NOT NULL,
                       status VARCHAR(50) NOT NULL DEFAULT 'PUBLIC',
                       category VARCHAR(100),
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                       FOREIGN KEY (userid) REFERENCES users(user_id)
);

-- Tạo bảng post_comments
CREATE TABLE post_comments (
                               comment_id INT PRIMARY KEY AUTO_INCREMENT,
                               postid INT NOT NULL,
                               userid INT,
                               parent_commentid INT,
                               comment_text TEXT NOT NULL,
                               status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                               FOREIGN KEY (postid) REFERENCES posts(post_id),
                               FOREIGN KEY (userid) REFERENCES users(user_id),
                               FOREIGN KEY (parent_commentid) REFERENCES post_comments(comment_id)
);

-- Tạo bảng comment_images
CREATE TABLE comment_images (
                                image_id INT PRIMARY KEY AUTO_INCREMENT,
                                commentid INT NOT NULL,
                                image_url VARCHAR(255) NOT NULL,
                                caption TEXT,
                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                FOREIGN KEY (commentid) REFERENCES post_comments(comment_id)
);

-- Tạo bảng comment_likes
CREATE TABLE comment_likes (
                               like_id INT PRIMARY KEY AUTO_INCREMENT,
                               commentid INT NOT NULL,
                               userid INT NOT NULL,
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               is_active TINYINT NOT NULL DEFAULT 1,
                               FOREIGN KEY (commentid) REFERENCES post_comments(comment_id),
                               FOREIGN KEY (userid) REFERENCES users(user_id)
);

-- Tạo bảng comment_mentions
CREATE TABLE comment_mentions (
                                  mention_id INT PRIMARY KEY AUTO_INCREMENT,
                                  commentid INT NOT NULL,
                                  mentioned_userid INT NOT NULL,
                                  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                  FOREIGN KEY (commentid) REFERENCES post_comments(comment_id),
                                  FOREIGN KEY (mentioned_userid) REFERENCES users(user_id)
);

-- Tạo bảng comment_reactions
CREATE TABLE comment_reactions (
                                   reaction_id INT PRIMARY KEY AUTO_INCREMENT,
                                   commentid INT NOT NULL,
                                   userid INT NOT NULL,
                                   reaction_type VARCHAR(50) NOT NULL,
                                   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                   FOREIGN KEY (commentid) REFERENCES post_comments(comment_id),
                                   FOREIGN KEY (userid) REFERENCES users(user_id)
);

-- Tạo bảng comment_reports
CREATE TABLE comment_reports (
                                 report_id INT PRIMARY KEY AUTO_INCREMENT,
                                 commentid INT NOT NULL,
                                 userid INT NOT NULL,
                                 report_reason TEXT NOT NULL,
                                 status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
                                 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 FOREIGN KEY (commentid) REFERENCES post_comments(comment_id),
                                 FOREIGN KEY (userid) REFERENCES users(user_id)
);

-- Tạo bảng copyright_claims
CREATE TABLE copyright_claims (
                                  claim_id INT PRIMARY KEY AUTO_INCREMENT,
                                  videoid INT NOT NULL,
                                  claimant_userid INT,
                                  claimant_name VARCHAR(255) NOT NULL,
                                  claimant_email VARCHAR(100),
                                  claim_reason TEXT NOT NULL,
                                  claim_evidence TEXT,
                                  claim_status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
                                  claim_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                  resolved_at TIMESTAMP,
                                  adminid INT,
                                  FOREIGN KEY (videoid) REFERENCES videos(video_id),
                                  FOREIGN KEY (claimant_userid) REFERENCES users(user_id),
                                  FOREIGN KEY (adminid) REFERENCES admins(admin_id)
);

-- Tạo bảng copyright_disputes
CREATE TABLE copyright_disputes (
                                    dispute_id INT PRIMARY KEY AUTO_INCREMENT,
                                    claimid INT NOT NULL,
                                    videoid INT NOT NULL,
                                    userid INT NOT NULL,
                                    dispute_reason TEXT NOT NULL,
                                    dispute_evidence TEXT,
                                    dispute_status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
                                    dispute_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                    resolved_at TIMESTAMP,
                                    adminid INT,
                                    FOREIGN KEY (claimid) REFERENCES copyright_claims(claim_id),
                                    FOREIGN KEY (videoid) REFERENCES videos(video_id),
                                    FOREIGN KEY (userid) REFERENCES users(user_id),
                                    FOREIGN KEY (adminid) REFERENCES admins(admin_id)
);

-- Tạo bảng copyright_actions
CREATE TABLE copyright_actions (
                                   action_id INT PRIMARY KEY AUTO_INCREMENT,
                                   claimid INT,
                                   disputeid INT,
                                   videoid INT NOT NULL,
                                   adminid INT NOT NULL,
                                   action_type VARCHAR(50) NOT NULL,
                                   action_reason TEXT NOT NULL,
                                   action_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                   FOREIGN KEY (claimid) REFERENCES copyright_claims(claim_id),
                                   FOREIGN KEY (disputeid) REFERENCES copyright_disputes(dispute_id),
                                   FOREIGN KEY (videoid) REFERENCES videos(video_id),
                                   FOREIGN KEY (adminid) REFERENCES admins(admin_id)
);

-- Tạo bảng monetization_status
CREATE TABLE monetization_status (
                                     monetization_id INT PRIMARY KEY AUTO_INCREMENT,
                                     userid INT NOT NULL,
                                     status VARCHAR(50) NOT NULL DEFAULT 'DISABLED',
                                     application_date TIMESTAMP,
                                     approval_date TIMESTAMP,
                                     rejection_reason TEXT,
                                     total_watch_hours DECIMAL(10,2) NOT NULL DEFAULT 0.00,
                                     total_subscribers INT NOT NULL DEFAULT 0,
                                     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                     updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                     FOREIGN KEY (userid) REFERENCES users(user_id)
);

-- Tạo bảng notifications
CREATE TABLE notifications (
                               notification_id INT PRIMARY KEY AUTO_INCREMENT,
                               userid INT NOT NULL,
                               senderid INT,
                               notification_type VARCHAR(50) NOT NULL,
                               reference_id INT NOT NULL,
                               reference_type VARCHAR(50) NOT NULL,
                               message VARCHAR(255) NOT NULL,
                               is_read TINYINT NOT NULL DEFAULT 0,
                               read_at TIMESTAMP,
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               FOREIGN KEY (userid) REFERENCES users(user_id),
                               FOREIGN KEY (senderid) REFERENCES users(user_id)
);

-- Tạo bảng payment_gateway_transactions
CREATE TABLE payment_gateway_transactions (
                                              gateway_transaction_id INT PRIMARY KEY AUTO_INCREMENT,
                                              userid INT NOT NULL,
                                              payment_method VARCHAR(100) NOT NULL,
                                              gateway_type VARCHAR(50) NOT NULL,
                                              gateway_transaction_reference VARCHAR(100),
                                              amount DECIMAL(10,2) NOT NULL,
                                              gateway_fee DECIMAL(10,2) NOT NULL DEFAULT 0.00,
                                              status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
                                              response_code VARCHAR(50),
                                              response_message TEXT,
                                              transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                              FOREIGN KEY (userid) REFERENCES users(user_id)
);

-- Tạo bảng user_wallets
CREATE TABLE user_wallets (
                              wallet_id INT PRIMARY KEY AUTO_INCREMENT,
                              userid INT NOT NULL,
                              balance DECIMAL(10,2) NOT NULL DEFAULT 0.00,
                              currency VARCHAR(3) NOT NULL DEFAULT 'USD',
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                              FOREIGN KEY (userid) REFERENCES users(user_id)
);

-- Tạo bảng payout_requests
CREATE TABLE payout_requests (
                                 payout_id INT PRIMARY KEY AUTO_INCREMENT,
                                 userid INT NOT NULL,
                                 walletid INT NOT NULL,
                                 amount DECIMAL(10,2) NOT NULL,
                                 fee DECIMAL(10,2) NOT NULL DEFAULT 0.00,
                                 net_amount DECIMAL(10,2) NOT NULL,
                                 payment_method VARCHAR(100) NOT NULL,
                                 status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
                                 request_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 processed_date TIMESTAMP,
                                 rejection_reason TEXT,
                                 FOREIGN KEY (userid) REFERENCES users(user_id),
                                 FOREIGN KEY (walletid) REFERENCES user_wallets(wallet_id)
);

-- Tạo bảng plan_features
CREATE TABLE plan_features (
                               planid INT NOT NULL,
                               featureid INT NOT NULL,
                               PRIMARY KEY (planid, featureid),
                               FOREIGN KEY (planid) REFERENCES subscription_plans(plan_id),
                               FOREIGN KEY (featureid) REFERENCES paid_features(feature_id)
);

-- Tạo bảng platform_ad_revenue
CREATE TABLE platform_ad_revenue (
                                     revenue_id INT PRIMARY KEY AUTO_INCREMENT,
                                     campaignid INT NOT NULL,
                                     total_views INT NOT NULL DEFAULT 0,
                                     cpm DECIMAL(10,2) NOT NULL,
                                     revenue_amount DECIMAL(10,2) NOT NULL,
                                     revenue_date DATE NOT NULL,
                                     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                     FOREIGN KEY (campaignid) REFERENCES admin_ad_campaigns(campaign_id)
);

-- Tạo bảng post_images
CREATE TABLE post_images (
                             image_id INT PRIMARY KEY AUTO_INCREMENT,
                             postid INT NOT NULL,
                             image_url VARCHAR(255) NOT NULL,
                             caption TEXT,
                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             FOREIGN KEY (postid) REFERENCES posts(post_id)
);

-- Tạo bảng post_likes
CREATE TABLE post_likes (
                            like_id INT PRIMARY KEY AUTO_INCREMENT,
                            postid INT NOT NULL,
                            userid INT NOT NULL,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            is_active TINYINT NOT NULL DEFAULT 1,
                            FOREIGN KEY (postid) REFERENCES posts(post_id),
                            FOREIGN KEY (userid) REFERENCES users(user_id)
);

-- Tạo bảng post_mentions
CREATE TABLE post_mentions (
                               mention_id INT PRIMARY KEY AUTO_INCREMENT,
                               postid INT NOT NULL,
                               mentioned_userid INT NOT NULL,
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               FOREIGN KEY (postid) REFERENCES posts(post_id),
                               FOREIGN KEY (mentioned_userid) REFERENCES users(user_id)
);

-- Tạo bảng post_shares
CREATE TABLE post_shares (
                             share_id INT PRIMARY KEY AUTO_INCREMENT,
                             postid INT NOT NULL,
                             userid INT NOT NULL,
                             share_platform VARCHAR(50) NOT NULL DEFAULT 'OTHER',
                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             FOREIGN KEY (postid) REFERENCES posts(post_id),
                             FOREIGN KEY (userid) REFERENCES users(user_id)
);

-- Tạo bảng post_views
CREATE TABLE post_views (
                            view_id INT PRIMARY KEY AUTO_INCREMENT,
                            postid INT NOT NULL,
                            userid INT NOT NULL,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            FOREIGN KEY (postid) REFERENCES posts(post_id),
                            FOREIGN KEY (userid) REFERENCES users(user_id)
);

-- Tạo bảng role_permissions
CREATE TABLE role_permissions (
                                  roleid INT NOT NULL,
                                  permissionid INT NOT NULL,
                                  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                  PRIMARY KEY (roleid, permissionid),
                                  FOREIGN KEY (roleid) REFERENCES roles(role_id),
                                  FOREIGN KEY (permissionid) REFERENCES permissions(permission_id)
);

-- Tạo bảng statistics
CREATE TABLE statistics (
                            stat_id INT PRIMARY KEY AUTO_INCREMENT,
                            userid INT,
                            stat_type VARCHAR(50) NOT NULL DEFAULT 'DAILY',
                            stat_date DATE NOT NULL,
                            total_video_views INT NOT NULL DEFAULT 0,
                            total_likes INT NOT NULL DEFAULT 0,
                            total_subscriptions INT NOT NULL DEFAULT 0,
                            total_users_who_watched INT NOT NULL DEFAULT 0,
                            total_users_subscribed INT NOT NULL DEFAULT 0,
                            total_users_liked INT NOT NULL DEFAULT 0,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            FOREIGN KEY (userid) REFERENCES users(user_id)
);

-- Tạo bảng transactions
CREATE TABLE transactions (
                              transaction_id INT PRIMARY KEY AUTO_INCREMENT,
                              userid INT NOT NULL,
                              planid INT NOT NULL,
                              amount DECIMAL(10,2) NOT NULL,
                              payment_method VARCHAR(50) NOT NULL,
                              transaction_reference VARCHAR(100),
                              transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
                              FOREIGN KEY (userid) REFERENCES users(user_id),
                              FOREIGN KEY (planid) REFERENCES subscription_plans(plan_id)
);

-- Tạo bảng user_playback_settings
CREATE TABLE user_playback_settings (
                                        setting_id INT PRIMARY KEY AUTO_INCREMENT,
                                        userid INT NOT NULL,
                                        default_playback_speed DECIMAL(3,1) NOT NULL DEFAULT 1.0,
                                        default_video_quality VARCHAR(50) NOT NULL DEFAULT 'AUTO',
                                        default_stop_after_minutes INT,
                                        default_is_loop_enabled TINYINT NOT NULL DEFAULT 0,
                                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                        FOREIGN KEY (userid) REFERENCES users(user_id)
);

-- Tạo bảng user_subscriptions
CREATE TABLE user_subscriptions (
                                    subscription_id INT PRIMARY KEY AUTO_INCREMENT,
                                    userid INT NOT NULL,
                                    planid INT NOT NULL,
                                    start_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                    end_date TIMESTAMP,
                                    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
                                    FOREIGN KEY (userid) REFERENCES users(user_id),
                                    FOREIGN KEY (planid) REFERENCES subscription_plans(plan_id)
);

-- Tạo bảng video_access_logs
CREATE TABLE video_access_logs (
                                   log_id INT PRIMARY KEY AUTO_INCREMENT,
                                   videoid INT NOT NULL,
                                   userid INT NOT NULL,
                                   access_type VARCHAR(50) NOT NULL,
                                   access_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                   FOREIGN KEY (videoid) REFERENCES videos(video_id),
                                   FOREIGN KEY (userid) REFERENCES users(user_id)
);

-- Tạo bảng video_ad_views
CREATE TABLE video_ad_views (
                                view_id INT PRIMARY KEY AUTO_INCREMENT,
                                videoid INT NOT NULL,
                                campaignid INT NOT NULL,
                                userid INT NOT NULL,
                                view_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                FOREIGN KEY (videoid) REFERENCES videos(video_id),
                                FOREIGN KEY (campaignid) REFERENCES ad_campaigns(campaign_id),
                                FOREIGN KEY (userid) REFERENCES users(user_id)
);

-- Tạo bảng video_category_assignments
CREATE TABLE video_category_assignments (
                                            assignment_id INT PRIMARY KEY AUTO_INCREMENT,
                                            videoid INT NOT NULL,
                                            categoryid INT NOT NULL,
                                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                            FOREIGN KEY (videoid) REFERENCES videos(video_id),
                                            FOREIGN KEY (categoryid) REFERENCES video_categories(category_id)
);

-- Tạo bảng video_comments
CREATE TABLE video_comments (
                                comment_id INT PRIMARY KEY AUTO_INCREMENT,
                                videoid INT NOT NULL,
                                userid INT NOT NULL,
                                parent_commentid INT,
                                comment_text TEXT NOT NULL,
                                status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                FOREIGN KEY (videoid) REFERENCES videos(video_id),
                                FOREIGN KEY (userid) REFERENCES users(user_id),
                                FOREIGN KEY (parent_commentid) REFERENCES video_comments(comment_id)
);

-- Tạo bảng video_comment_images
CREATE TABLE video_comment_images (
                                      image_id INT PRIMARY KEY AUTO_INCREMENT,
                                      commentid INT NOT NULL,
                                      image_url VARCHAR(255) NOT NULL,
                                      caption TEXT,
                                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                      FOREIGN KEY (commentid) REFERENCES video_comments(comment_id)
);

-- Tạo bảng video_comment_mentions
CREATE TABLE video_comment_mentions (
                                        mention_id INT PRIMARY KEY AUTO_INCREMENT,
                                        commentid INT NOT NULL,
                                        mentioned_userid INT NOT NULL,
                                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                        FOREIGN KEY (commentid) REFERENCES video_comments(comment_id),
                                        FOREIGN KEY (mentioned_userid) REFERENCES users(user_id)
);

-- Tạo bảng video_comment_reactions
CREATE TABLE video_comment_reactions (
                                         reaction_id INT PRIMARY KEY AUTO_INCREMENT,
                                         commentid INT NOT NULL,
                                         userid INT NOT NULL,
                                         reaction_type VARCHAR(50) NOT NULL,
                                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                         FOREIGN KEY (commentid) REFERENCES video_comments(comment_id),
                                         FOREIGN KEY (userid) REFERENCES users(user_id)
);

-- Tạo bảng video_likes
CREATE TABLE video_likes (
                             like_id INT PRIMARY KEY AUTO_INCREMENT,
                             videoid INT NOT NULL,
                             userid INT NOT NULL,
                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             is_active TINYINT NOT NULL DEFAULT 1,
                             FOREIGN KEY (videoid) REFERENCES videos(video_id),
                             FOREIGN KEY (userid) REFERENCES users(user_id)
);

-- Tạo bảng video_processing_logs
CREATE TABLE video_processing_logs (
                                       log_id INT PRIMARY KEY AUTO_INCREMENT,
                                       videoid INT NOT NULL,
                                       processing_details TEXT,
                                       processing_status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
                                       start_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                       end_time TIMESTAMP,
                                       error_message TEXT,
                                       progress INT,
                                       FOREIGN KEY (videoid) REFERENCES videos(video_id)
);

-- Tạo bảng video_progress
CREATE TABLE video_progress (
                                progress_id INT PRIMARY KEY AUTO_INCREMENT,
                                videoid INT NOT NULL,
                                userid INT NOT NULL,
                                current_time INT,
                                duration INT,
                                video_quality VARCHAR(50) NOT NULL DEFAULT 'AUTO',
                                last_watched TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                is_completed TINYINT NOT NULL DEFAULT 0,
                                FOREIGN KEY (videoid) REFERENCES videos(video_id),
                                FOREIGN KEY (userid) REFERENCES users(user_id)
);

-- Tạo bảng video_reports
CREATE TABLE video_reports (
                               report_id INT PRIMARY KEY AUTO_INCREMENT,
                               videoid INT NOT NULL,
                               userid INT NOT NULL,
                               reason TEXT,
                               report_type VARCHAR(50) NOT NULL,
                               status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               reviewed_at TIMESTAMP,
                               reviewed_by_adminid INT,
                               admin_comment TEXT,
                               FOREIGN KEY (videoid) REFERENCES videos(video_id),
                               FOREIGN KEY (userid) REFERENCES users(user_id),
                               FOREIGN KEY (reviewed_by_adminid) REFERENCES admins(admin_id)
);

-- Tạo bảng video_tag_assignments
CREATE TABLE video_tag_assignments (
                                       assignment_id INT PRIMARY KEY AUTO_INCREMENT,
                                       videoid INT NOT NULL,
                                       tagid INT NOT NULL,
                                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                       FOREIGN KEY (videoid) REFERENCES videos(video_id),
                                       FOREIGN KEY (tagid) REFERENCES video_tags(tag_id)
);

-- Tạo bảng video_views
CREATE TABLE video_views (
                             view_id INT PRIMARY KEY AUTO_INCREMENT,
                             videoid INT NOT NULL,
                             userid INT NOT NULL,
                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             FOREIGN KEY (videoid) REFERENCES videos(video_id),
                             FOREIGN KEY (userid) REFERENCES users(user_id)
);

-- Tạo bảng video_watch_later
CREATE TABLE video_watch_later (
                                   watch_later_id INT PRIMARY KEY AUTO_INCREMENT,
                                   videoid INT NOT NULL,
                                   userid INT NOT NULL,
                                   saved_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                   FOREIGN KEY (videoid) REFERENCES videos(video_id),
                                   FOREIGN KEY (userid) REFERENCES users(user_id)
);

-- Tạo bảng wallet_transactions
CREATE TABLE wallet_transactions (
                                     transaction_id INT PRIMARY KEY AUTO_INCREMENT,
                                     walletid INT NOT NULL,
                                     amount DECIMAL(10,2) NOT NULL,
                                     transaction_type VARCHAR(50) NOT NULL,
                                     reference_id VARCHAR(100),
                                     description TEXT,
                                     transaction_status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
                                     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                     completed_at TIMESTAMP,
                                     error_message TEXT,
                                     payment_method VARCHAR(50),
                                     FOREIGN KEY (walletid) REFERENCES user_wallets(wallet_id)
);


CREATE TABLE video_progress (
                                progress_id INT NOT NULL AUTO_INCREMENT,
                                watch_time INT, -- Thời điểm hiện tại của video (giây)
                                duration INT, -- Tổng thời lượng video (giây)
                                is_completed BIT, -- Trạng thái hoàn thành (0: chưa hoàn thành, 1: đã hoàn thành)
                                last_watched TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Thời điểm xem gần nhất
                                video_quality ENUM('AUTO', 'P_240', 'P_360', 'P_480', 'P_720', 'P_1080', 'P_4K') NOT NULL, -- Chất lượng video
                                userid INT NOT NULL, -- ID của người dùng
                                videoid INT NOT NULL, -- ID của video
                                PRIMARY KEY (progress_id),
                                FOREIGN KEY (userid) REFERENCES users(id), -- Giả định bảng users có cột id
                                FOREIGN KEY (videoid) REFERENCES videos(id) -- Giả định bảng videos có cột id
) ENGINE=InnoDB;



-- Gán quyền cho ADMIN (role_id = 1, có tất cả quyền)
INSERT INTO role_permissions (roleid, permissionid)
SELECT 1, permission_id FROM permissions WHERE permission_name IN (
                                                                   'CREATE_ROOM', 'VIEW_ROOM', 'UPDATE_ROOM', 'DELETE_ROOM', 'ROOM_MANAGEMENT',
                                                                   'CREATE_ROOM_CATEGORY', 'VIEW_ROOM_CATEGORY', 'UPDATE_ROOM_CATEGORY', 'DELETE_ROOM_CATEGORY', 'ROOM_CATEGORY_MANAGEMENT',
                                                                   'CREATE_EMPLOYEE', 'VIEW_EMPLOYEE', 'UPDATE_EMPLOYEE', 'DELETE_EMPLOYEE', 'EMPLOYEE_MANAGEMENT',
                                                                   'CREATE_USER', 'VIEW_USER', 'UPDATE_USER', 'DELETE_USER', 'USER_MANAGEMENT',
                                                                   'VIEW_ROLE', 'CREATE_ROLE', 'UPDATE_ROLE', 'DELETE_ROLE', 'ROLE_MANAGEMENT',
                                                                   'VIEW_ACTIVITY_LOG',
                                                                   'CREATE_POST', 'VIEW_POST', 'UPDATE_POST', 'DELETE_POST', 'POST_MANAGEMENT',
                                                                   'CREATE_VIDEO', 'VIEW_VIDEO', 'UPDATE_VIDEO', 'DELETE_VIDEO', 'VIDEO_MANAGEMENT',
                                                                   'CREATE_COMMENT', 'VIEW_COMMENT', 'UPDATE_COMMENT', 'DELETE_COMMENT', 'COMMENT_MANAGEMENT',
                                                                   'CREATE_VIDEO_COMMENT', 'VIEW_VIDEO_COMMENT', 'UPDATE_VIDEO_COMMENT', 'DELETE_VIDEO_COMMENT', 'VIDEO_COMMENT_MANAGEMENT',
                                                                   'CREATE_AD_CAMPAIGN', 'VIEW_AD_CAMPAIGN', 'UPDATE_AD_CAMPAIGN', 'DELETE_AD_CAMPAIGN', 'AD_CAMPAIGN_MANAGEMENT',
                                                                   'VIEW_NOTIFICATION', 'MANAGE_NOTIFICATION',
                                                                   'VIEW_STATISTICS', 'MANAGE_STATISTICS',
                                                                   'FOLLOW_CHANNEL', 'UNFOLLOW_CHANNEL',
                                                                   'LIKE_VIDEO', 'UNLIKE_VIDEO',
                                                                   'LIKE_POST', 'UNLIKE_POST',
                                                                   'VIEW_WATCH_HISTORY', 'MANAGE_WATCH_HISTORY'
    );

-- Gán quyền cho USER (role_id = 2, quyền hạn chế)
INSERT INTO role_permissions (roleid, permissionid)
SELECT 2, permission_id FROM permissions WHERE permission_name IN (
                                                                   'VIEW_PROFILE', 'EDIT_PROFILE', 'VIEW_USER_DASHBOARD',
                                                                   'CREATE_POST', 'VIEW_POST', 'UPDATE_POST',
                                                                   'CREATE_VIDEO', 'VIEW_VIDEO', 'UPDATE_VIDEO',
                                                                   'CREATE_COMMENT', 'VIEW_COMMENT', 'UPDATE_COMMENT',
                                                                   'CREATE_VIDEO_COMMENT', 'VIEW_VIDEO_COMMENT', 'UPDATE_VIDEO_COMMENT',
                                                                   'VIEW_NOTIFICATION',
                                                                   'VIEW_STATISTICS',
                                                                   'FOLLOW_CHANNEL', 'UNFOLLOW_CHANNEL',
                                                                   'LIKE_VIDEO', 'UNLIKE_VIDEO',
                                                                   'LIKE_POST', 'UNLIKE_POST',
                                                                   'VIEW_WATCH_HISTORY'
    );

-- Gán quyền cho MANAGER (role_id = 3, quyền xem là chính)
INSERT INTO role_permissions (roleid, permissionid)
SELECT 3, permission_id FROM permissions WHERE permission_name IN (
                                                                   'VIEW_PROFILE', 'EDIT_PROFILE', 'VIEW_MANAGER_DASHBOARD',
                                                                   'VIEW_POST', 'VIEW_VIDEO', 'VIEW_COMMENT',
                                                                   'VIEW_VIDEO_COMMENT',
                                                                   'VIEW_NOTIFICATION',
                                                                   'VIEW_STATISTICS'
    );



INSERT INTO permissions (permission_id, created_at, description, permission_name) VALUES
                                                                                      (5, '2025-05-01 08:00:00', 'Tạo bài viết', 'CREATE_POST'),
                                                                                      (6, '2025-05-01 08:00:00', 'Xem bài viết', 'VIEW_POST'),
                                                                                      (7, '2025-05-01 08:00:00', 'Sửa bài viết', 'UPDATE_POST'),
                                                                                      (8, '2025-05-01 08:00:00', 'Xóa bài viết', 'DELETE_POST'),
                                                                                      (9, '2025-05-01 08:00:00', 'Quản lý tất cả bài viết', 'POST_MANAGEMENT'),
                                                                                      (10, '2025-05-01 08:00:00', 'Tạo video', 'CREATE_VIDEO'),
                                                                                      (11, '2025-05-01 08:00:00', 'Xem video', 'VIEW_VIDEO'),
                                                                                      (12, '2025-05-01 08:00:00', 'Sửa video', 'UPDATE_VIDEO'),
                                                                                      (13, '2025-05-01 08:00:00', 'Xóa video', 'DELETE_VIDEO'),
                                                                                      (14, '2025-05-01 08:00:00', 'Tạo bình luận', 'CREATE_COMMENT'),
                                                                                      (15, '2025-05-01 08:00:00', 'Xem bình luận', 'VIEW_COMMENT'),
                                                                                      (16, '2025-05-01 08:00:00', 'Sửa bình luận', 'UPDATE_COMMENT'),
                                                                                      (17, '2025-05-01 08:00:00', 'Xóa bình luận', 'DELETE_COMMENT'),
                                                                                      (18, '2025-05-01 08:00:00', 'Quản lý tất cả bình luận', 'COMMENT_MANAGEMENT');