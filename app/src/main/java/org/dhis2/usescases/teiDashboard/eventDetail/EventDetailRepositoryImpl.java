package org.dhis2.usescases.teiDashboard.eventDetail;

import android.content.ContentValues;

import com.squareup.sqlbrite2.BriteDatabase;

import org.dhis2.data.tuples.Pair;
import org.dhis2.utils.DateUtils;
import org.hisp.dhis.android.core.category.CategoryComboModel;
import org.hisp.dhis.android.core.category.CategoryOptionComboModel;
import org.hisp.dhis.android.core.common.State;
import org.hisp.dhis.android.core.enrollment.EnrollmentModel;
import org.hisp.dhis.android.core.enrollment.EnrollmentStatus;
import org.hisp.dhis.android.core.event.EventModel;
import org.hisp.dhis.android.core.event.EventStatus;
import org.hisp.dhis.android.core.organisationunit.OrganisationUnitModel;
import org.hisp.dhis.android.core.program.ProgramModel;
import org.hisp.dhis.android.core.program.ProgramStage;
import org.hisp.dhis.android.core.program.ProgramStageDataElementModel;
import org.hisp.dhis.android.core.program.ProgramStageSectionModel;
import org.hisp.dhis.android.core.trackedentity.TrackedEntityDataValueModel;
import org.hisp.dhis.android.core.trackedentity.TrackedEntityInstanceModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;

import static org.dhis2.data.database.SqlConstants.ALL;
import static org.dhis2.data.database.SqlConstants.AND;
import static org.dhis2.data.database.SqlConstants.EQUAL;
import static org.dhis2.data.database.SqlConstants.FROM;
import static org.dhis2.data.database.SqlConstants.JOIN;
import static org.dhis2.data.database.SqlConstants.LIMIT_1;
import static org.dhis2.data.database.SqlConstants.NOT_EQUAL;
import static org.dhis2.data.database.SqlConstants.ON;
import static org.dhis2.data.database.SqlConstants.ORDER_BY;
import static org.dhis2.data.database.SqlConstants.POINT;
import static org.dhis2.data.database.SqlConstants.PROGRAM_STAGE_TABLE;
import static org.dhis2.data.database.SqlConstants.PROGRAM_STAGE_UID;
import static org.dhis2.data.database.SqlConstants.QUESTION_MARK;
import static org.dhis2.data.database.SqlConstants.QUOTE;
import static org.dhis2.data.database.SqlConstants.SELECT;
import static org.dhis2.data.database.SqlConstants.TABLE_POINT_FIELD;
import static org.dhis2.data.database.SqlConstants.TABLE_POINT_FIELD_EQUALS;
import static org.dhis2.data.database.SqlConstants.VARIABLE;
import static org.dhis2.data.database.SqlConstants.WHERE;

/**
 * QUADRAM. Created by ppajuelo on 02/11/2017.
 */

public class EventDetailRepositoryImpl implements EventDetailRepository {

    private static final String ORG_UNIT_NAME = "SELECT OrganisationUnit.displayName FROM OrganisationUnit " +
            "JOIN Event ON Event.organisationUnit = OrganisationUnit.uid " +
            "WHERE Event.uid = ? " +
            "LIMIT 1";
    private static final String ORG_UNIT = "SELECT OrganisationUnit.* FROM OrganisationUnit " +
            "JOIN Event ON Event.organisationUnit = OrganisationUnit.uid " +
            "WHERE Event.uid = ? " +
            "LIMIT 1";

    private final BriteDatabase briteDatabase;
    private final String eventUid;
    private final String teiUid;


    EventDetailRepositoryImpl(BriteDatabase briteDatabase, String eventUid, String teiUid) {
        this.briteDatabase = briteDatabase;
        this.eventUid = eventUid;
        this.teiUid = teiUid;
    }

    @NonNull
    @Override
    public Observable<EventModel> eventModelDetail(String uid) {
        String selectEventWithUid = SELECT + ALL + FROM + EventModel.TABLE + WHERE + EventModel.Columns.UID + EQUAL + QUOTE + uid + QUOTE +
                AND + EventModel.TABLE + POINT + EventModel.Columns.STATE + NOT_EQUAL + QUOTE + State.TO_DELETE + QUOTE + LIMIT_1;
        return briteDatabase.createQuery(EventModel.TABLE, selectEventWithUid)
                .mapToOne(EventModel::create);
    }

    @NonNull
    @Override
    public Observable<List<ProgramStageSectionModel>> programStageSection(String eventUid) {
        String selectProgramStageSections = String.format(
                SELECT + VARIABLE + POINT + ALL + FROM + VARIABLE +
                        JOIN + VARIABLE + ON + TABLE_POINT_FIELD_EQUALS + TABLE_POINT_FIELD +
                        WHERE + TABLE_POINT_FIELD_EQUALS + QUESTION_MARK +
                        AND + EventModel.TABLE + POINT + EventModel.Columns.STATE + NOT_EQUAL + QUOTE + State.TO_DELETE + QUOTE +
                        ORDER_BY + TABLE_POINT_FIELD,
                ProgramStageSectionModel.TABLE, ProgramStageSectionModel.TABLE,
                EventModel.TABLE, EventModel.TABLE, EventModel.Columns.PROGRAM_STAGE, ProgramStageSectionModel.TABLE, ProgramStageSectionModel.Columns.PROGRAM_STAGE,
                EventModel.TABLE, EventModel.Columns.UID,
                ProgramStageSectionModel.TABLE, ProgramStageSectionModel.Columns.SORT_ORDER
        );
        return briteDatabase.createQuery(EventModel.TABLE, selectProgramStageSections, eventUid == null ? "" : eventUid)
                .mapToList(ProgramStageSectionModel::create);
    }

    @NonNull
    @Override
    public Observable<List<ProgramStageDataElementModel>> programStageDataElement(String eventUid) {
        String selectProgramStageDe = String.format(
                SELECT + VARIABLE + POINT + ALL + FROM + VARIABLE +
                        JOIN + VARIABLE + ON + TABLE_POINT_FIELD_EQUALS + TABLE_POINT_FIELD +
                        WHERE + TABLE_POINT_FIELD_EQUALS + QUESTION_MARK +
                        AND + EventModel.TABLE + POINT + EventModel.Columns.STATE + NOT_EQUAL + QUOTE + State.TO_DELETE + QUOTE,
                ProgramStageDataElementModel.TABLE, ProgramStageDataElementModel.TABLE,
                EventModel.TABLE, EventModel.TABLE, EventModel.Columns.PROGRAM_STAGE, ProgramStageDataElementModel.TABLE, ProgramStageDataElementModel.Columns.PROGRAM_STAGE,
                EventModel.TABLE, EventModel.Columns.UID
        );
        return briteDatabase.createQuery(EventModel.TABLE, selectProgramStageDe, eventUid == null ? "" : eventUid)
                .mapToList(ProgramStageDataElementModel::create);
    }

    @NonNull
    @Override
    public Observable<List<TrackedEntityDataValueModel>> dataValueModelList(String eventUid) {
        String selectTrackedEntityDataValueWithEventUid = SELECT + ALL + FROM + TrackedEntityDataValueModel.TABLE +
                WHERE + TrackedEntityDataValueModel.Columns.EVENT + EQUAL;
        String uid = eventUid == null ? "" : eventUid;
        return briteDatabase.createQuery(TrackedEntityDataValueModel.TABLE, selectTrackedEntityDataValueWithEventUid + QUOTE + uid + QUOTE)
                .mapToList(TrackedEntityDataValueModel::create);
    }

    @NonNull
    @Override
    public Observable<ProgramStage> programStage(String eventUid) {
        String query = SELECT + PROGRAM_STAGE_TABLE + POINT + ALL + FROM + PROGRAM_STAGE_TABLE +
                JOIN + EventModel.TABLE + ON + EventModel.TABLE + POINT + EventModel.Columns.PROGRAM_STAGE +
                EQUAL + PROGRAM_STAGE_TABLE + POINT + PROGRAM_STAGE_UID +
                WHERE + EventModel.TABLE + POINT + EventModel.Columns.UID +
                EQUAL + QUESTION_MARK + LIMIT_1;
        return briteDatabase.createQuery(PROGRAM_STAGE_TABLE, query, eventUid == null ? "" : eventUid)
                .mapToOne(ProgramStage::create);
    }

    @Override
    public void deleteNotPostedEvent(String eventUid) {
        String deleteWhere = String.format(
                TABLE_POINT_FIELD_EQUALS,
                EventModel.TABLE, EventModel.Columns.UID
        );
        String id = eventUid == null ? "" : eventUid;
        briteDatabase.delete(EventModel.TABLE, deleteWhere + QUOTE + id + QUOTE);
    }

    @Override
    public void deletePostedEvent(EventModel eventModel) {
        Date currentDate = Calendar.getInstance().getTime();
        EventModel event = EventModel.builder()
                .id(eventModel.id())
                .uid(eventModel.uid())
                .created(eventModel.created())
                .lastUpdated(currentDate)
                .eventDate(eventModel.eventDate())
                .dueDate(eventModel.dueDate())
                .enrollment(eventModel.enrollment())
                .program(eventModel.program())
                .programStage(eventModel.programStage())
                .organisationUnit(eventModel.organisationUnit())
                .status(eventModel.status())
                .state(State.TO_DELETE)
                .build();

        if (event != null) {
            briteDatabase.update(EventModel.TABLE, event.toContentValues(),
                    EventModel.Columns.UID + EQUAL + QUESTION_MARK, event.uid());
            updateTEi();
        }


        updateProgramTable(currentDate, eventModel.program());
    }

    @NonNull
    @Override
    public Observable<String> orgUnitName(String eventUid) {
        return briteDatabase.createQuery(OrganisationUnitModel.TABLE, ORG_UNIT_NAME, eventUid == null ? "" : eventUid)
                .mapToOne(cursor -> cursor.getString(0));
    }

    @NonNull
    @Override
    public Observable<OrganisationUnitModel> orgUnit(String eventUid) {
        return briteDatabase.createQuery(OrganisationUnitModel.TABLE, ORG_UNIT, eventUid == null ? "" : eventUid)
                .mapToOne(OrganisationUnitModel::create);
    }

    @Override
    public Observable<List<OrganisationUnitModel>> getOrgUnits() {
        String eventOrgUnits = "SELECT OrganisationUnit.* FROM OrganisationUnit " +
                "JOIN OrganisationUnitProgramLink ON OrganisationUnitProgramLink.organisationUnit = OrganisationUnit.uid " +
                "JOIN Event ON Event.program = OrganisationUnitProgramLink.program " +
                "WHERE Event.uid = ?";
        return briteDatabase.createQuery(OrganisationUnitModel.TABLE, eventOrgUnits, eventUid).mapToList(OrganisationUnitModel::create);
    }

    @Override
    public Observable<Pair<String, List<CategoryOptionComboModel>>> getCategoryOptionCombos() {
        String getCatComboFromEvent = "SELECT CategoryCombo.* FROM CategoryCombo " +
                "WHERE CategoryCombo.uid IN (" +
                "SELECT Program.categoryCombo FROM Program " +
                "JOIN Event ON Event.program = Program.uid " +
                "WHERE Event.uid = ? LIMIT 1)";
        String selectCategoryCombo = String.format("SELECT * FROM %s WHERE %s.%s = ?",
                CategoryOptionComboModel.TABLE, CategoryOptionComboModel.TABLE, CategoryOptionComboModel.Columns.CATEGORY_COMBO);
        return briteDatabase.createQuery(CategoryComboModel.TABLE, getCatComboFromEvent, eventUid == null ? "" : eventUid)
                .mapToOne(CategoryComboModel::create)
                .flatMap(catCombo -> {
                    if (catCombo != null && !catCombo.isDefault())
                        return briteDatabase.createQuery(CategoryOptionComboModel.TABLE, selectCategoryCombo, catCombo.uid()).mapToList(CategoryOptionComboModel::create)
                                .map(list -> Pair.create(catCombo.name(), list));
                    else
                        return Observable.just(Pair.create("", new ArrayList<>()));
                });
    }

    @NonNull
    @Override
    public Flowable<EventStatus> eventStatus(String eventUid) {
        return briteDatabase.createQuery(EventModel.TABLE, "SELECT Event.status FROM Event WHERE Event.uid = ? LIMIT 1", eventUid == null ? "" : eventUid)
                .mapToOne(cursor -> EventStatus.valueOf(cursor.getString(0))).toFlowable(BackpressureStrategy.LATEST);
    }

    @Override
    public Observable<ProgramModel> getProgram(String eventUid) {
        return briteDatabase.createQuery(ProgramModel.TABLE, "SELECT Program.* FROM Program JOIN Event ON Event.program = Program.uid WHERE Event.uid = ? LIMIT 1", eventUid == null ? "" : eventUid)
                .mapToOne(ProgramModel::create);
    }

    @Override
    public void saveCatOption(CategoryOptionComboModel selectedOption) {
        ContentValues event = new ContentValues();
        event.put(EventModel.Columns.ATTRIBUTE_OPTION_COMBO, selectedOption.uid());
        event.put(EventModel.Columns.STATE, State.TO_UPDATE.name()); // TODO: Check if state is TO_POST
        // TODO: and if so, keep the TO_POST state

        briteDatabase.update(EventModel.TABLE, event, EventModel.Columns.UID + " = ?", eventUid == null ? "" : eventUid);
        updateTEi();
    }

    @Override
    public Observable<Boolean> isEnrollmentActive(String eventUid) {
        return briteDatabase.createQuery(EnrollmentModel.TABLE,
                "SELECT Enrollment.* FROM Enrollment " +
                        "JOIN Event ON Event.enrollment = Enrollment.uid " +
                        "WHERE Event.uid = ?", eventUid)
                .mapToOne(EnrollmentModel::create)
                .map(enrollment -> enrollment.enrollmentStatus() == EnrollmentStatus.ACTIVE);
    }

    @SuppressWarnings({"squid:S1172", "squid:CommentedOutCodeLine"})
    private void updateProgramTable(Date lastUpdated, String programUid) {
       /* ContentValues program = new ContentValues();  TODO: Crash if active
        program.put(EnrollmentModel.Columns.LAST_UPDATED, BaseIdentifiableObject.DATE_FORMAT.format(lastUpdated));
        briteDatabase.update(ProgramModel.TABLE, program, ProgramModel.Columns.UID + " = ?", programUid);*/
    }

    private void updateTEi() {
        ContentValues tei = new ContentValues();
        tei.put(TrackedEntityInstanceModel.Columns.LAST_UPDATED, DateUtils.databaseDateFormat().format(Calendar.getInstance().getTime()));
        tei.put(TrackedEntityInstanceModel.Columns.STATE, State.TO_UPDATE.name());// TODO: Check if state is TO_POST
        // TODO: and if so, keep the TO_POST state
        briteDatabase.update(TrackedEntityInstanceModel.TABLE, tei, "uid = ?", teiUid);
    }
}