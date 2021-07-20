CRON_JOB_ID="$1"
CRON_JOB="$2"
LOG_DIR="/var/log/cronlogs"
LOG_FILE="$(date +%Y-%m-%d)_$CRON_JOB_ID.log"

if [[ ! -e $LOG_DIR/status ]]; then
    mkdir -p $LOG_DIR/status
fi
if [[ ! -e $LOG_DIR/running ]]; then
    mkdir -p $LOG_DIR/running
fi

# During RUNNING
$CRON_JOB >> $LOG_DIR/running/$LOG_FILE 2>&1 & echo $!
CRON_PID=$!
# Start
DATESTAMP=$(date +%Y-%m-%d)
TIMESTAMP=$(date +%H:%M:%S.%3N)
MESSAGE_TYPE="Start"
CONTENT="Test Start Log for Webhook Service."
echo "$DATESTAMP $TIMESTAMP $CRON_JOB_ID $CRON_PID $MESSAGE_TYPE: $CONTENT" >> "$LOG_DIR/status/$LOG_FILE"

# End
DATESTAMP=$(date +%Y-%m-%d)
TIMESTAMP=$(date +%H:%M:%S.%3N)
MESSAGE_TYPE="End"
CONTENT="Test End Log for Webhook Service."
echo "$DATESTAMP $TIMESTAMP $CRON_JOB_ID $CRON_PID $MESSAGE_TYPE: $CONTENT" >> "$LOG_DIR/status/$LOG_FILE"